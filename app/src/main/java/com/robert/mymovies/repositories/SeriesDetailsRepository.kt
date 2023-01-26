package com.robert.mymovies.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.robert.mymovies.data.remote.MoviesAPI
import com.robert.mymovies.data.remote.responses.SeriesDetailsResponse
import com.robert.mymovies.data.remote.responses.CastResponse
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.utils.Resource
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

class SeriesDetailsRepository@Inject constructor(
    private val api: MoviesAPI,
    private val app: Application): RepositorySeriesDetails {


    override suspend fun getSeriesCastDetails(filmId: Int): Resource<CastResponse> {
        return try {
            if (checkForInternet()){
                val response = handleCastResponse(api.getSeriesCredits(filmId = filmId))
                Resource(Resource.Status.SUCCESS, response.data, null)
            }else{
                Resource(Resource.Status.ERROR, null, "No internet connection")
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> Resource(Resource.Status.ERROR, null, "Connection Time out")
                else -> Resource(Resource.Status.ERROR, null, "Conversion Error")
            }
        }
    }

    override suspend fun getSimilarSeries(filmId: Int): Resource<FilmResponse> {
        return try {
            if (checkForInternet()){
                val response = handleSimilarResponse(api.getSimilarSeries(filmId = filmId))
                Resource(Resource.Status.SUCCESS, response.data, null)
            }else{
                Resource(Resource.Status.ERROR, null, "No internet connection")
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> Resource(Resource.Status.ERROR, null, "Connection Time out")
                else -> Resource(Resource.Status.ERROR, null, "Conversion Error")
            }
        }
    }

    override suspend fun getSeriesDetails(filmId: Int): Resource<SeriesDetailsResponse> {
        return try {
            if (checkForInternet()){
                val response = handleSeriesDetailsResponse(api.getSeriesDetails(filmId = filmId))
                Resource(Resource.Status.SUCCESS, response.data, null)
            }else{
                Resource(Resource.Status.ERROR, null, "No internet connection")
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> Resource(Resource.Status.ERROR, null, "Connection Time out")
                else -> Resource(Resource.Status.ERROR, null, "Conversion Error")
            }
        }
    }

    private fun handleCastResponse(response: Response<CastResponse>): Resource<CastResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource(Resource.Status.SUCCESS, resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, "Unknown Error")
    }

    private fun handleSimilarResponse(response: Response<FilmResponse>): Resource<FilmResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource(Resource.Status.SUCCESS, resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }

    private fun handleSeriesDetailsResponse(response: Response<SeriesDetailsResponse>): Resource<SeriesDetailsResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource(Resource.Status.SUCCESS, resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }


    private fun checkForInternet(): Boolean {
        // register activity with the connectivity manager service
        val connectivityManager = app.getSystemService(
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