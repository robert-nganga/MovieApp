package com.robert.mymovies.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.robert.mymovies.data.remote.MovieResponse
import com.robert.mymovies.repositories.Repository
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MoreFilmsFragmentViewModel@Inject constructor(
    app: Application,
    private val repository: Repository): AndroidViewModel(app) {

    private val _allFilms: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val allFilms: LiveData<Resource<MovieResponse>>
        get() = _allFilms
    var allFilmsPage = 1

    private var allFilmsResponse: MovieResponse? = null

    private fun handleResponse(response: Response<MovieResponse>): Resource<MovieResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                allFilmsPage++
                if (allFilmsResponse == null){
                    allFilmsResponse = resultResponse
                }else{
                    val oldMovies = allFilmsResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                return  Resource(Resource.Status.SUCCESS, allFilmsResponse ?:resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }


    fun getPopularMovies() = viewModelScope.launch {
        _allFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if (checkForInternet()){
                val result = repository.getPopularMovies(allFilmsPage)
                _allFilms.postValue(handleResponse(result))
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


    fun getUpcomingMovies() = viewModelScope.launch {
        _allFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        try {
            if (checkForInternet()){
                val result = repository.getUpcomingMovies(allFilmsPage)
                _allFilms.postValue(handleResponse(result))
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