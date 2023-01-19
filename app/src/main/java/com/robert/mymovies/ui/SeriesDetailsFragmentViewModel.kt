package com.robert.mymovies.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.robert.mymovies.data.remote.*
import com.robert.mymovies.data.remote.responses.CastResponse
import com.robert.mymovies.repositories.RepositorySeries
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class SeriesDetailsFragmentViewModel @Inject constructor(
        app: Application,
        private val repository: RepositorySeries):AndroidViewModel(app) {

    var seriesId: Int? = null

    private var _seriesDetails = MutableLiveData<Resource<SeriesDetailsResponse>>()
    val seriesDetails: LiveData<Resource<SeriesDetailsResponse>>
        get() = _seriesDetails

    private var _similarSeries = MutableLiveData<Resource<SeriesResponse>>()
    val similarSeries: LiveData<Resource<SeriesResponse>>
        get() = _similarSeries


    private var _castDetails = MutableLiveData<Resource<CastResponse>>()
    val castDetails: LiveData<Resource<CastResponse>>
        get() = _castDetails


    fun fetchData(seriesId: Int){
        getSeriesDetails(seriesId)
        getCastDetails(seriesId)
        getSimilarSeries(seriesId)
    }

    fun getSeriesDetails(seriesId: Int) = viewModelScope.launch {
        _seriesDetails.postValue(Resource(Resource.Status.LOADING, null, null))

        try {
            if (checkForInternet()){
                val result = repository.getSeriesDetails(seriesId)
                _seriesDetails.postValue(handleSeriesDetailsResponse(result))
            } else{
                _seriesDetails.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    _seriesDetails.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
                }
                else -> _seriesDetails.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
            }
        }
    }

    fun getCastDetails(seriesId: Int) = viewModelScope.launch {
        _castDetails.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if (checkForInternet()){
                val result = repository.getSeriesCredits(seriesId)
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

    fun getSimilarSeries(seriesId: Int) = viewModelScope.launch {
        _similarSeries.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if (checkForInternet()){
                val result = repository.getSimilarSeries(seriesId)
                _similarSeries.postValue(handleSimilarSeriesResponse(result))
            } else{
                _similarSeries.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    _similarSeries.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
                }
                else -> _similarSeries.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
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

    private fun handleSimilarSeriesResponse(response: Response<SeriesResponse>): Resource<SeriesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource(Resource.Status.SUCCESS, resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }

    private fun handleSeriesDetailsResponse(response: Response<SeriesDetailsResponse>): Resource<SeriesDetailsResponse> {
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