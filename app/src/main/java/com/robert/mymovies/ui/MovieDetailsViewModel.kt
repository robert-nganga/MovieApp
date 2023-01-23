package com.robert.mymovies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.mymovies.data.remote.MovieDetailsResponse
import com.robert.mymovies.data.remote.SeriesDetailsResponse
import com.robert.mymovies.data.remote.responses.CastResponse
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.repositories.RepositoryMovieDetails
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel@Inject constructor(
        private val repository: RepositoryMovieDetails): ViewModel() {

    var movieId: Int? = null

    private val _similarMovies: MutableLiveData<Resource<FilmResponse>> = MutableLiveData()
    val similarMovies: LiveData<Resource<FilmResponse>> get() =  _similarMovies

    private var _movieDetails: MutableLiveData<Resource<MovieDetailsResponse>> = MutableLiveData()
    val movieDetails: LiveData<Resource<MovieDetailsResponse>> get() = _movieDetails

    private var _movieCast: MutableLiveData<Resource<CastResponse>> = MutableLiveData()
    val movieCast: LiveData<Resource<CastResponse>> get() =  _movieCast


    fun fetchData(filmId: Int){
        getMovieDetails(filmId)
        getMovieCast(filmId)
        getSimilarMovie(filmId)
    }


    private fun getMovieDetails(filmId: Int) = viewModelScope.launch {
        _movieDetails.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getMovieDetails(filmId = filmId)
        _movieDetails.postValue(result)
    }

    private fun getMovieCast(filmId: Int) = viewModelScope.launch {
        _movieCast.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getMovieCast(filmId = filmId)
        _movieCast.postValue(result)
    }

    private fun getSimilarMovie(filmId: Int) = viewModelScope.launch {
        _similarMovies.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getSimilarMovies(filmId = filmId)
        _similarMovies.postValue(result)
    }

}