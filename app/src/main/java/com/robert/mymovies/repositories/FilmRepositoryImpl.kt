package com.robert.mymovies.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.robert.mymovies.data.remote.MoviesAPI
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.data.remote.responses.GenreResponse
import com.robert.mymovies.utils.FilmType
import com.robert.mymovies.utils.Resource
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

class FilmRepositoryImpl@Inject constructor(
        private val app: Application,
        private val api: MoviesAPI
): FilmRepository {

    override suspend fun getPopularFilms(page: Int, filmType: FilmType): Resource<FilmResponse> {
        if (filmType == FilmType.MOVIE){
            return try {
                if (checkForInternet()){
                    val response = handleFilmResponse(api.getPopularMovies(page = page))
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
        }else{
            return try {
                if (checkForInternet()){
                    val response = handleFilmResponse(api.getPopularSeries(page = page))
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
    }

    override suspend fun getUpcomingFilms(page: Int, filmType: FilmType): Resource<FilmResponse> {
        return try {
            if (checkForInternet()){
                val response = handleFilmResponse(api.getUpcomingMovies(page = page))
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

    override suspend fun getTrendingFilms(filmType: FilmType): Resource<FilmResponse> {
        if (filmType == FilmType.MOVIE){
            return try {
                if (checkForInternet()){
                    val response = handleFilmResponse(api.getTrendingMovies())
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
        }else{
            return try {
                if (checkForInternet()){
                    val response = handleFilmResponse(api.getTrendingSeries())
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
    }

    override suspend fun getOnAirFilms(page: Int, filmType: FilmType): Resource<FilmResponse> {
        return try {
            if (checkForInternet()){
                val response = handleFilmResponse(api.getOnAirSeries(page = page))
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

    override suspend fun getTopRatedFilms(page: Int, filmType: FilmType): Resource<FilmResponse> {
        if (filmType == FilmType.MOVIE){
            return try {
                if (checkForInternet()){
                    val response = handleFilmResponse(api.getTopRatedMovies(page = page))
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
        }else{
            return try {
                if (checkForInternet()){
                    val response = handleFilmResponse(api.getTopRatedSeries(page = page))
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
    }

    override suspend fun getLatestFilms(filmType: FilmType): Resource<FilmResponse> {
        if (filmType == FilmType.MOVIE){
            return try {
                if (checkForInternet()){
                    val response = handleFilmResponse(api.getLatestMovies())
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
        }else{
            return try {
                if (checkForInternet()){
                    val response = handleFilmResponse(api.getLatestSeries())
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
    }

    override suspend fun getGenreList(filmType: FilmType): Resource<GenreResponse> {
        if (filmType == FilmType.MOVIE){
            return try {
                if (checkForInternet()){
                    val response = handleGenreResponse(api.getMoviesGenreList())
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
        }else{
            return try {
                if (checkForInternet()){
                    val response = handleGenreResponse(api.getSeriesGenreList())
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

    }

    private fun handleFilmResponse(response: Response<FilmResponse>): Resource<FilmResponse> {
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