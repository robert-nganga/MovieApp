package com.robert.mymovies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


    fun getMovieDetails(movieId: Int) = viewModelScope.launch {
        _movieDetails.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getMovieDetails(movieId)
        _movieDetails.postValue(handleResponse(result))
    }

    private fun handleResponse(response: Response<MovieDetailsResponse>): Resource<MovieDetailsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource(Resource.Status.SUCCESS, resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }
}