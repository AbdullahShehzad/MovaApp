package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelMovies : ViewModel() {

    private val _topMovies = MutableStateFlow<List<ModelImage>>(emptyList())
    val topMovies: StateFlow<List<ModelImage>> = _topMovies.asStateFlow()

    private val _newReleases = MutableStateFlow<List<ModelImage>>(emptyList())
    val newReleases: StateFlow<List<ModelImage>> = _newReleases.asStateFlow()

    init {
        fetchTop10Movies()
        fetchNewReleases()
    }

    fun fetchTop10Movies(num: Int = 0) {
        viewModelScope.launch {
            if (num == 1)
                pageNumTopRated = num
            val response = Network.movieService.getTop10Movies(pageNumTopRated)
            if (response.isSuccessful) {
                val body = response.body()
                body?.getAsJsonArray("results")?.let { results ->
                    val images = results.map {
                        ModelImage(
                            url = it.asJsonObject.getAsJsonPrimitive("poster_path").asString,
                            rating = it.asJsonObject.getAsJsonPrimitive("vote_average").asDouble
                        )
                    }
                    if (num == 1) {
                        _topMovies.value = images
                    } else {
                        _topMovies.update { it + images }
                    }
                }
            }
            ++pageNumTopRated
        }
    }

    fun fetchNewReleases() {
        viewModelScope.launch {
            val response = Network.movieService.getNewReleases(pageNumNewRelease)
            if (response.isSuccessful) {
                val body = response.body()
                body?.getAsJsonArray("results")?.let { results ->
                    val images = results.map {
                        ModelImage(
                            url = it.asJsonObject.getAsJsonPrimitive("poster_path").asString,
                            rating = it.asJsonObject.getAsJsonPrimitive("vote_average").asDouble
                        )
                    }
                    _newReleases.update { it + images }
                }
            }
            ++pageNumNewRelease
        }
    }

    fun filteredMovies(title: String) {
        viewModelScope.launch {
            if (pageNumFilter == 1)
                _topMovies.value = emptyList()
            val response = Network.movieService.FilterMovies(title, pageNumFilter)
            if (response.isSuccessful) {
                val body = response.body()
                body?.getAsJsonArray("results")?.let { results ->
                    val images = results.map { result ->
                        val jsonObject = result.asJsonObject
                        val posterPath =
                            if (jsonObject.get("poster_path") != null && !jsonObject.get("poster_path").isJsonNull) {
                                jsonObject.getAsJsonPrimitive("poster_path").asString
                            } else if (jsonObject.get("backdrop_path") != null && !jsonObject.get("backdrop_path").isJsonNull) {
                                jsonObject.getAsJsonPrimitive("backdrop_path").asString
                            } else {
                                "null"  // Image Place Holder.
                            }
                        val rating =
                            if (jsonObject.get("vote_average") != null && !jsonObject.get("vote_average").isJsonNull) {
                                jsonObject.getAsJsonPrimitive("vote_average").asDouble
                            } else {
                                0.00
                            }
                        ModelImage(
                            url = posterPath,
                            rating = rating
                        )
                    }
                    _topMovies.update { it + images }
                }
            }
            ++pageNumFilter
        }
    }

    fun clearData() {
        _topMovies.value = emptyList()
    }

    fun changeFilterNum() {
        pageNumFilter = 1
    }

    companion object {
        var pageNumTopRated = 1
        var pageNumNewRelease = 1
        var pageNumFilter = 1
    }
}