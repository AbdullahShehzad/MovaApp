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
                            url = it.asJsonObject.getAsJsonPrimitive("poster_path")
                                .asString,
                            rating = it.asJsonObject.getAsJsonPrimitive("vote_average")
                                .asDouble,
                            name = it.asJsonObject.getAsJsonPrimitive("title")
                                .asString
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
                            rating = it.asJsonObject.getAsJsonPrimitive("vote_average").asDouble,
                            name = it.asJsonObject.getAsJsonPrimitive("title")
                                .asString
                        )
                    }
                    _newReleases.update { it + images }
                }
            }
            ++pageNumNewRelease
        }
    }


    companion object {
        var pageNumTopRated = 1
        var pageNumNewRelease = 1
    }
}