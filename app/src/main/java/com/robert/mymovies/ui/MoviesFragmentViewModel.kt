package com.robert.mymovies.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.robert.mymovies.data.remote.GenreResponse
import com.robert.mymovies.data.remote.MovieResponse
import com.robert.mymovies.repositories.Repository
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MoviesFragmentViewModel @Inject constructor(app: Application, private val repository: Repository): AndroidViewModel(app) {


    private val _allPopularMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val allPopularMovies: LiveData<Resource<MovieResponse>>
        get() = _allPopularMovies

    private val _allUpcomingMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val allUpcomingMovies: LiveData<Resource<MovieResponse>>
        get() = _allUpcomingMovies

    private val _allTrendingMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val allTrendingMovies: LiveData<Resource<MovieResponse>>
        get() = _allTrendingMovies

    private val _allGenres: MutableLiveData<Resource<GenreResponse>> = MutableLiveData()
    val allGenres: LiveData<Resource<GenreResponse>>
        get() = _allGenres

    init {
        fetchData()
    }

    fun fetchData() = viewModelScope.launch {
        getGenreList()
        delay(100L)
        getTrendingMovies()
        delay(100L)
        getPopularMovies()
        delay(100L)
        getUpcomingMovies()
    }

    private fun handleMovieResponse(response: Response<MovieResponse>): Resource<MovieResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource(Resource.Status.SUCCESS, resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }

    private fun handleGenreResponse(response: Response<GenreResponse>): Resource<GenreResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource(Resource.Status.SUCCESS, resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }

    private fun getPopularMovies() = viewModelScope.launch {
        _allPopularMovies.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if(checkForInternet()){
                val result = repository.getPopularMovies(2)
                _allPopularMovies.postValue(handleMovieResponse(result))
            }else{
                _allPopularMovies.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> _allPopularMovies.postValue(Resource(Resource.Status.ERROR, null, "Network Failure"))
                else -> _allPopularMovies.postValue(Resource(Resource.Status.ERROR, null, "Conversion Error"))
            }
        }
    }

    private fun getUpcomingMovies() = viewModelScope.launch {
        _allUpcomingMovies.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if(checkForInternet()){
                val result = repository.getUpcomingMovies(2)
                _allUpcomingMovies.postValue(handleMovieResponse(result))
            }else{
                _allUpcomingMovies.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> _allUpcomingMovies.postValue(Resource(Resource.Status.ERROR, null, "Network Failure"))
                else -> _allUpcomingMovies.postValue(Resource(Resource.Status.ERROR, null, "Conversion Error"))
            }
        }
    }

    private fun getTrendingMovies() = viewModelScope.launch {
        _allTrendingMovies.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if(checkForInternet()){
                val result = repository.getTrendingMovies()
                _allTrendingMovies.postValue(handleMovieResponse(result))
            }else{
                _allTrendingMovies.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> _allTrendingMovies.postValue(Resource(Resource.Status.ERROR, null, "Network Failure"))
                else -> _allTrendingMovies.postValue(Resource(Resource.Status.ERROR, null, "Conversion Error"))
            }
        }
    }

    private fun getGenreList() = viewModelScope.launch {
        _allGenres.value = Resource(Resource.Status.LOADING, null, null)
        try {
            if(checkForInternet()){
                val result = repository.getGenreList()
                _allGenres.postValue(handleGenreResponse(result))
            }else{
                _allGenres.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> _allGenres.postValue(Resource(Resource.Status.ERROR, null, "Network Failure"))
                else -> _allGenres.postValue(Resource(Resource.Status.ERROR, null, "Conversion Error"))
            }
        }
    }

    private fun checkForInternet(): Boolean {
        // register activity with the connectivity manager service
        val connectivityManager = getApplication<com.robert.mymovies.Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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