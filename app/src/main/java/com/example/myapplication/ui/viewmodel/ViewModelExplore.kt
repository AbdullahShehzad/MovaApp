package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.MovaApp
import com.example.myapplication.R
import com.example.myapplication.data.model.ModelChipSelection
import com.example.myapplication.data.model.ModelMovie
import com.example.myapplication.data.repository.MovieRepository
import com.example.myapplication.util.Network
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelExplore(
    private val movieRepository: MovieRepository = MovieRepository(
        MovaApp.database.movieDao(), Network.movieService
    )
) : ViewModel() {

    private var pageNumTopRated = 1
    private var pageNumFilter = 1
    private var advPageFilter = 1

    private val _topMovies = MutableStateFlow<List<ModelMovie>>(emptyList())
    val topMovies: StateFlow<List<ModelMovie>> = _topMovies.asStateFlow()

    private val _myList = MovaApp.database.movieDao().getAll()
    val myList: StateFlow<List<ModelMovie>> =
        _myList.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

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
        if (num == 1) pageNumTopRated = num

        viewModelScope.launch {
            val images = movieRepository.fetchTop10Movies(pageNumTopRated)
            if (num == 1) {
                _topMovies.value = images
            } else {
                _topMovies.update { it + images }
            }
            ++pageNumTopRated
        }
    }


    fun filteredMovies(title: String) {
        if (pageNumFilter == 1) {
            _topMovies.value = emptyList()
        }
        viewModelScope.launch {
            val images = movieRepository.filteredMovies(title, pageNumFilter)
            _topMovies.update { it + images }
            ++pageNumFilter
        }

    }

    fun advancedMovieFilter(region: String, genre: String, year: Int, sort: String) {
        if (pageNumFilter == 1) {
            _topMovies.value = emptyList()
        }
        viewModelScope.launch {
            val images =
                movieRepository.advancedFilteredMovies(region, genre, year, sort, advPageFilter)
            _topMovies.update { it + images }
            ++advPageFilter
        }
    }

    fun addMovieToDB(imageId: Int, imageURL: String?, imageRatings: Double, movieName: String) {
        viewModelScope.launch {
            movieRepository.addMovieToDatabase(imageId, imageURL, imageRatings, movieName)
        }
    }

    fun updateRegion(id: Int) {
        _chipSettings.update { it.copy(region = id) }
    }

    fun updateGenre(ids: List<Int>) {
        _chipSettings.update { it.copy(genre = ids) }
    }

    fun updateTime(id: Int) {
        _chipSettings.update { it.copy(time = id) }
    }

    fun updateSort(id: Int) {
        _chipSettings.update { it.copy(sort = id) }
    }

    fun clearData() {
        _topMovies.update { emptyList() }
    }

    fun changeFilterNum() {
        pageNumFilter = 1
    }
}