package com.robert.mymovies.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.robert.mymovies.data.remote.MovieResponse
import com.robert.mymovies.data.remote.SeriesResponse
import com.robert.mymovies.repositories.RepositorySeries
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MoreSeriesFragmentViewModel@Inject constructor(app: Application, private val repository: RepositorySeries): AndroidViewModel(app) {


    private val _allFilms: MutableLiveData<Resource<SeriesResponse>> = MutableLiveData()
    val allFilms: LiveData<Resource<SeriesResponse>>
        get() = _allFilms
    var allFilmsPage = 1

    private var allFilmsResponse: SeriesResponse? = null


    private fun handleSeriesResponse(response: Response<SeriesResponse>): Resource<SeriesResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                allFilmsPage++
                if (allFilmsResponse == null){
                    allFilmsResponse = resultResponse
                }else{
                    val oldSeries = allFilmsResponse?.results
                    val newSeries = resultResponse.results
                    oldSeries?.addAll(newSeries)
                }
                return  Resource(Resource.Status.SUCCESS, allFilmsResponse ?: resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }

    fun getPopularSeries() = viewModelScope.launch {
        _allFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if (checkForInternet()){
                val result = repository.getPopularSeries(allFilmsPage)
                _allFilms.postValue(handleSeriesResponse(result))
            } else{
                _allFilms.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    _allFilms.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
                }
                else -> _allFilms.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
            }
        }
    }

    fun getOnAirSeries() = viewModelScope.launch {
        _allFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if (checkForInternet()){
                val result = repository.getOnAirSeries(allFilmsPage)
                _allFilms.postValue(handleSeriesResponse(result))
            } else{
                _allFilms.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    _allFilms.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
                }
                else -> _allFilms.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
            }
        }
    }

    fun getTopRatedSeries() = viewModelScope.launch {
        _allFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if (checkForInternet()){
                val result = repository.getTopRatedSeries(allFilmsPage)
                _allFilms.postValue(handleSeriesResponse(result))
            } else{
                _allFilms.postValue(Resource(Resource.Status.ERROR, null, "No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    _allFilms.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
                }
                else -> _allFilms.postValue(Resource(Resource.Status.ERROR, null, "Connection Time out"))
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