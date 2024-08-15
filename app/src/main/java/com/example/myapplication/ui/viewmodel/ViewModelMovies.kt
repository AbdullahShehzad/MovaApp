package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.ModelMovie
import com.example.myapplication.MovaApp
import com.example.myapplication.data.repository.MovieRepository
import com.example.myapplication.util.Network
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelMovies(
    private val movieRepository: MovieRepository = MovieRepository(
        MovaApp.database.movieDao(),
        Network.movieService
    )
) : ViewModel() {

    var pageNumTopRated = 1
    var pageNumNewRelease = 1

    private val _topMovies = MutableStateFlow<List<ModelMovie>>(emptyList())
    val topMovies: StateFlow<List<ModelMovie>> = _topMovies.asStateFlow()

    private val _newReleases = MutableStateFlow<List<ModelMovie>>(emptyList())
    val newReleases: StateFlow<List<ModelMovie>> = _newReleases.asStateFlow()

    private val _myList = MovaApp.database.movieDao().getAll()
    val myList: StateFlow<List<ModelMovie>> =
        _myList.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        fetchTop10Movies(0)
        fetchNewReleases()
    }

    fun fetchTop10Movies(num: Int) {
        if (num == 1) pageNumTopRated = num
        viewModelScope.launch {
            val images = movieRepository.fetchTop10Movies(pageNumTopRated)
            if (num == 1)
                _topMovies.value = images
            else
                _topMovies.update { it + images }
            ++pageNumTopRated
        }
    }

    fun fetchNewReleases() {
        viewModelScope.launch {
            val images = movieRepository.fetchNewReleases(pageNumNewRelease)
            _newReleases.update { it + images }
            ++pageNumNewRelease
        }
    }

    fun addMovieToDB(imageId: Int, imageURL: String?, imageRatings: Double, movieName: String) {
        viewModelScope.launch {
            movieRepository.addMovieToDatabase(imageId, imageURL, imageRatings, movieName)
        }
    }

    fun removeMovieFromDB(imageId: Int, imageURL: String?, imageRatings: Double, movieName: String) {
        viewModelScope.launch {
            movieRepository.removeMovieFromDB(imageId, imageURL, imageRatings, movieName)
        }
    }
}