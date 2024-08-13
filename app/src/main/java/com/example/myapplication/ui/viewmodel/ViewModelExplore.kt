package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.ModelMovie
import com.example.myapplication.MovaApp
import com.example.myapplication.util.Network
import com.example.myapplication.R
import com.example.myapplication.ui.view.ModelChipSelection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelExplore : ViewModel() {

    private val _topMovies = MutableStateFlow<List<ModelMovie>>(emptyList())
    val topMovies: StateFlow<List<ModelMovie>> = _topMovies.asStateFlow()

    private val _myList = MovaApp.database.movieDao().getAll()
    val myList: StateFlow<List<ModelMovie>> = _myList.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    private val _chipSettings = MutableStateFlow(
        ModelChipSelection(
            R.id.allRegions, emptyList(), R.id.allPeriods, R.id.popularity
        )
    )
    val chipSettings = _chipSettings.asStateFlow()

    init {
        fetchTop10Movies()
    }

    fun fetchTop10Movies(num: Int = 0) {
        viewModelScope.launch {
            if (num == 1) pageNumTopRated = num
            val response = Network.movieService.getTop10Movies(pageNumTopRated)
            if (response.isSuccessful) {
                val body = response.body()
                body?.getAsJsonArray("results")?.let { results ->
                    val images = results.map {
                        ModelMovie(
                            url = it.asJsonObject.getAsJsonPrimitive("poster_path").asString,
                            rating = it.asJsonObject.getAsJsonPrimitive("vote_average").asDouble,
                            name = it.asJsonObject.getAsJsonPrimitive("title").asString
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

    fun filteredMovies(title: String) {
        viewModelScope.launch {
            if (pageNumFilter == 1) _topMovies.value = emptyList()
            val response = Network.movieService.filterMovies(title, pageNumFilter)
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
                        val movieName = jsonObject.getAsJsonPrimitive("title").asString
                        ModelMovie(
                            url = posterPath, rating = rating, name = movieName
                        )
                    }
                    _topMovies.update { it + images }
                }
            }
            ++pageNumFilter
        }
    }

    fun advancedMovieFilter(region: String, genre: String, year: Int, sort: String) {
        viewModelScope.launch {
            if (pageNumFilter == 1) _topMovies.value = emptyList()

            val response = Network.movieService.advancedMovieFilter(
                region, genre, year, sort, advPageFilter
            )

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
                        val movieName = jsonObject.getAsJsonPrimitive("title").asString
                        ModelMovie(
                            url = posterPath, rating = rating, name = movieName
                        )
                    }
                    _topMovies.update { it + images }
                }
            }
            ++advPageFilter
        }
    }

    fun updateRegion(id: Int) {
        chipSettings.value.region = id
    }

    fun updateGenre(ids: List<Int>) {
        chipSettings.value.genre = ids
    }

    fun updateTime(id: Int) {
        chipSettings.value.time = id
    }

    fun updateSort(id: Int) {
        chipSettings.value.sort = id
    }


    fun clearData() {
        _topMovies.value = emptyList()
    }

    fun changeFilterNum() {
        pageNumFilter = 1
    }

    companion object {
        var pageNumTopRated = 1
        var pageNumFilter = 1
        var advPageFilter = 1
    }
}