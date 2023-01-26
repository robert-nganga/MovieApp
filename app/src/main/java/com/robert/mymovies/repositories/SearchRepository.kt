package com.robert.mymovies.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.robert.mymovies.data.remote.MoviesAPI
import com.robert.mymovies.data.remote.responses.SearchResponse
import com.robert.mymovies.utils.Resource
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

class SearchRepository@Inject constructor(
        private val app: Application,
        private val api: MoviesAPI
): RepositorySearch {


    override suspend fun searchFilms(query: String, page: Int): Resource<SearchResponse> {
        return try {
            if (checkForInternet()){
                val response = handleSearchResponse(api.searchFilms(query = query, page = page))
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


    private fun handleSearchResponse(response: Response<SearchResponse>): Resource<SearchResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource(Resource.Status.SUCCESS, resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, "Unknown Error")
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