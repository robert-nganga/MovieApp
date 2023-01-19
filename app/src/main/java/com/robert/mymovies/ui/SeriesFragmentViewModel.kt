package com.robert.mymovies.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.robert.mymovies.data.remote.responses.GenreResponse
import com.robert.mymovies.data.remote.SeriesResponse
import com.robert.mymovies.repositories.RepositorySeries
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class SeriesFragmentViewModel@Inject constructor(
        private val repository: RepositorySeries,
        app: Application): AndroidViewModel(app) {


    private val _allPopularSeries: MutableLiveData<Resource<SeriesResponse>> = MutableLiveData()
    val allPopularSeries: LiveData<Resource<SeriesResponse>>
        get() = _allPopularSeries

    private val _allOnAirSeries: MutableLiveData<Resource<SeriesResponse>> = MutableLiveData()
    val allOnAirSeries: LiveData<Resource<SeriesResponse>>
        get() = _allOnAirSeries

    private val _allTopRatedSeries: MutableLiveData<Resource<SeriesResponse>> = MutableLiveData()
    val allTopRatedSeries: LiveData<Resource<SeriesResponse>>
        get() = _allTopRatedSeries

    private val _allSeriesGenres: MutableLiveData<Resource<GenreResponse>> = MutableLiveData()
    val allSeriesGenres: LiveData<Resource<GenreResponse>>
        get() = _allSeriesGenres

    init {
        fetchData()
    }

    fun fetchData() = viewModelScope.launch {
        getGenreList()
        delay(100L)
        getTopRatedSeries()
        delay(100L)
        getPopularSeries()
        delay(100L)
        getOnAirSeries()
    }

    private fun handleMovieResponse(response: Response<SeriesResponse>): Resource<SeriesResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource(Resource.Status.SUCCESS, resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }

    private fun handleGenreResponse(response: Response<GenreResponse>): Resource<GenreResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource(Resource.Status.SUCCESS, resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }

    private fun getPopularSeries() = viewModelScope.launch {
        _allPopularSeries.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if(checkForInternet()){
                val result = repository.getPopularSeries(1)
                _allPopularSeries.postValue(handleMovieResponse(result))
            }else{
                _allPopularSeries.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> _allPopularSeries.postValue(Resource(Resource.Status.ERROR, null, "Network Failure"))
                else -> _allPopularSeries.postValue(Resource(Resource.Status.ERROR, null, "Conversion Error"))
            }
        }
    }

    private fun getTopRatedSeries() = viewModelScope.launch {
        _allTopRatedSeries.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if(checkForInternet()){
                val result = repository.getTopRatedSeries(1)
                _allTopRatedSeries.postValue(handleMovieResponse(result))
            }else{
                _allTopRatedSeries.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> _allTopRatedSeries.postValue(Resource(Resource.Status.ERROR, null, "Network Failure"))
                else -> _allTopRatedSeries.postValue(Resource(Resource.Status.ERROR, null, "Conversion Error"))
            }
        }
    }

    private fun getOnAirSeries() = viewModelScope.launch {
        _allOnAirSeries.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if(checkForInternet()){
                val result = repository.getOnAirSeries(1)
                _allOnAirSeries.postValue(handleMovieResponse(result))
            }else{
                _allOnAirSeries.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> _allOnAirSeries.postValue(Resource(Resource.Status.ERROR, null, "Network Failure"))
                else -> _allOnAirSeries.postValue(Resource(Resource.Status.ERROR, null, "Conversion Error"))
            }
        }
    }

    private fun getGenreList() = viewModelScope.launch {
        _allSeriesGenres.value = Resource(Resource.Status.LOADING, null, null)
        try {
            if(checkForInternet()){
                val result = repository.getSeriesGenreList()
                _allSeriesGenres.postValue(handleGenreResponse(result))
            }else{
                _allSeriesGenres.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> _allSeriesGenres.postValue(Resource(Resource.Status.ERROR, null, "Network Failure"))
                else -> _allSeriesGenres.postValue(Resource(Resource.Status.ERROR, null, "Conversion Error"))
            }
        }
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