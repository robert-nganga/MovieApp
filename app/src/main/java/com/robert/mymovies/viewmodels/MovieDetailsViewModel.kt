package com.robert.mymovies.viewmodels

import androidx.lifecycle.*
import com.robert.mymovies.model.MovieDetails
import com.robert.mymovies.data.remote.responses.CastResponse
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.repositories.MovieDetailsRepository
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel@Inject constructor(
        private val repository: MovieDetailsRepository): ViewModel() {

    var movieId: Int? = null
    private val id = MutableLiveData<Int>()

    private val _similarMovies: MutableLiveData<Resource<FilmResponse>> = MutableLiveData()
    val similarMovies: LiveData<Resource<FilmResponse>> get() =  _similarMovies

    private var _movieCast: MutableLiveData<Resource<CastResponse>> = MutableLiveData()
    val movieCast: LiveData<Resource<CastResponse>> get() =  _movieCast

    val movieDetails = id.switchMap {
        repository.getMovieDetails(filmId = it).asLiveData()
    }


    fun fetchData(filmId: Int){
        getMovieCast(filmId)
        getSimilarMovie(filmId)
    }

    fun setMovieId(filmId: Int){
        id.postValue(filmId)
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