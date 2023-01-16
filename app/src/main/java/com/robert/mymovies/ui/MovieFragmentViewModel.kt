package com.robert.mymovies.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.robert.mymovies.data.remote.CastResponse
import com.robert.mymovies.data.remote.MovieDetailsResponse
import com.robert.mymovies.data.remote.MovieResponse
import com.robert.mymovies.repositories.Repository
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MovieFragmentViewModel@Inject constructor(
        app: Application, private val repository: Repository):AndroidViewModel(app) {

    var movieId: Int? = null

    private var _movieDetails = MutableLiveData<Resource<MovieDetailsResponse>>()
    val movieDetails: LiveData<Resource<MovieDetailsResponse>>
        get() = _movieDetails

    private var _similarMovies = MutableLiveData<Resource<MovieResponse>>()
    val similarMovies: LiveData<Resource<MovieResponse>>
        get() = _similarMovies


    private var _castDetails = MutableLiveData<Resource<CastResponse>>()
    val castDetails: LiveData<Resource<CastResponse>>
        get() = _castDetails


    fun fetchData(movieId: Int) {
        getMovieDetails(movieId)
        getCastDetails(movieId)
        getSimilarMovies(movieId)
    }


    fun getMovieDetails(movieId: Int) = viewModelScope.launch {
        _movieDetails.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if (checkForInternet()){
                val result = repository.getMovieDetails(movieId)
                _movieDetails.postValue(handleMovieDetailsResponse(result))
            } else{
                _movieDetails.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    _movieDetails.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
                }
                else -> _movieDetails.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
            }
        }
    }

    fun getCastDetails(movieId: Int) = viewModelScope.launch {
        _castDetails.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if (checkForInternet()){
                val result = repository.getMovieCredits(movieId)
                _castDetails.postValue(handleCastResponse(result))
            } else{
                _castDetails.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    _castDetails.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
                }
                else -> _castDetails.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
            }
        }
    }

    fun getSimilarMovies(movieId: Int) = viewModelScope.launch {
        _similarMovies.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if (checkForInternet()){
                val result = repository.getSimilarMovies(movieId)
                _similarMovies.postValue(handleSimilarMoviesResponse(result))
            } else{
                _similarMovies.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    _similarMovies.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
                }
                else -> _similarMovies.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
            }
        }
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

    private fun checkForInternet(): Boolean {
        // register activity with the connectivity manager service
        val connectivityManager = getApplication<com.robert.mymovies.Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Indicates this network uses a Wi-Fi transport,
            // or WiFi has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            // Indicates this network uses a Cellular transport. or
            // Cellular has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            // else return false
            else -> false
        }
    }
}