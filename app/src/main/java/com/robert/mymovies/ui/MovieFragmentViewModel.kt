package com.robert.mymovies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.mymovies.data.remote.CastResponse
import com.robert.mymovies.data.remote.MovieDetailsResponse
import com.robert.mymovies.data.remote.MovieResponse
import com.robert.mymovies.repositories.Repository
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MovieFragmentViewModel@Inject constructor(private val repository: Repository):ViewModel() {

    private var _movieDetails = MutableLiveData<Resource<MovieDetailsResponse>>()
    val movieDetails: LiveData<Resource<MovieDetailsResponse>>
        get() = _movieDetails

    private var _similarMovies = MutableLiveData<Resource<MovieResponse>>()
    val similarMovies: LiveData<Resource<MovieResponse>>
        get() = _similarMovies


    private var _castDetails = MutableLiveData<Resource<CastResponse>>()
    val castDetails: LiveData<Resource<CastResponse>>
        get() = _castDetails


    fun getMovieDetails(movieId: Int) = viewModelScope.launch {
        _movieDetails.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getMovieDetails(movieId)
        _movieDetails.postValue(handleMovieDetailsResponse(result))
    }

    fun getCastDetails(movieId: Int) = viewModelScope.launch {
        _castDetails.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getMovieCredits(movieId)
        _castDetails.postValue(handleCastResponse(result))
    }

    fun getSimilarMovies(movieId: Int) = viewModelScope.launch {
        _similarMovies.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getSimilarMovies(movieId)
        _similarMovies.postValue(handleSimilarMoviesResponse(result))
    }

    private fun handleCastResponse(response: Response<CastResponse>): Resource<CastResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource(Resource.Status.SUCCESS, resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }

    private fun handleSimilarMoviesResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource(Resource.Status.SUCCESS, resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }

    private fun handleMovieDetailsResponse(response: Response<MovieDetailsResponse>): Resource<MovieDetailsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource(Resource.Status.SUCCESS, resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }
}