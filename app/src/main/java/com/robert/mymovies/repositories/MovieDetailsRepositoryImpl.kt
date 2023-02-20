package com.robert.mymovies.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.room.withTransaction
import com.robert.mymovies.data.local.FilmDatabase
import com.robert.mymovies.data.remote.MoviesAPI
import com.robert.mymovies.data.remote.responses.CastResponse
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.model.Film
import com.robert.mymovies.model.MovieDetails
import com.robert.mymovies.utils.Resource
import com.robert.mymovies.utils.networkBoundResource
import okio.IOException
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MovieDetailsRepositoryImpl@Inject constructor(
        private val app: Application,
        private val database: FilmDatabase,
        private val api: MoviesAPI
): MovieDetailsRepository {

    private val movieDetailsDao = database.movieDetailsDao()
    private val time = Date().toString()

    private fun shouldFetchFromNetwork(movie: MovieDetails): Boolean {
        return try{
        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val lastFetchTime = dateFormat.parse(movie.timeStamp)
        val difference = lastFetchTime?.let { (Date().time - lastFetchTime.time) / (1000 * 60 * 60 * 24) }
        difference != null && difference >= 1
        }catch (e: Exception){
            true
        }
    }


    override fun getMovieDetails(filmId: Int) = networkBoundResource(
        query = {
                movieDetailsDao.getMovieById(filmId)
        },
        fetch = {
            api.getMovieDetails(filmId = filmId)
        },
        saveFetchResult = { response ->
            val movie = response.body()?.copy(timeStamp = time)
            movie?.let {
                database.withTransaction {
                    movieDetailsDao.deleteMovie(filmId)
                    movieDetailsDao.insertMovie(movie)
                }
            }
        },
        shouldFetch = { movie -> shouldFetchFromNetwork(movie) }
    )

    override suspend fun getMovieCast(filmId: Int): Resource<CastResponse> {
        return try {
            if (checkForInternet()){
                val response = handleCastResponse(api.getMovieCredits(filmId = filmId))
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

    override suspend fun getSimilarMovies(filmId: Int): Resource<FilmResponse> {
        return try {
            if (checkForInternet()){
                val response = handleSimilarResponse(api.getSimilarMovies(filmId = filmId))
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