package com.robert.mymovies.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.room.withTransaction
import com.robert.mymovies.data.local.FilmDatabase
import com.robert.mymovies.data.remote.MoviesAPI
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.data.remote.responses.GenreResponse
import com.robert.mymovies.utils.FilmType
import com.robert.mymovies.utils.Resource
import com.robert.mymovies.utils.networkBoundResource
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

class FilmRepositoryImpl@Inject constructor(
        private val app: Application,
        private val database: FilmDatabase,
        private val api: MoviesAPI
): FilmRepository {

    private val filmDao = database.filmDao()


    override fun getPopularFilms(filmType: FilmType) = networkBoundResource(
        query = { if (filmType == FilmType.MOVIE) { filmDao.getMovies("popular") } else { filmDao.getTvShows("popular") } },
        fetch = { if (filmType == FilmType.MOVIE) { api.getPopularMovies() } else { api.getPopularSeries() } },
        saveFetchResult = { response ->
            val films = if (filmType == FilmType.MOVIE) {
                response.body()?.results?.map { it.copy(mediaType = "movie", category = "popular") }
            } else {
                response.body()?.results?.map { it.copy(mediaType = "tv", category = "popular") }
            }
            films?.let {
                database.withTransaction {
                    if (filmType == FilmType.MOVIE) {
                        filmDao.deleteMovies("popular")
                        filmDao.insertFilms(it)
                    } else {
                        filmDao.deleteTvShows("popular")
                        filmDao.insertFilms(it)
                    }
                }
            }
        }
    )

    override fun getUpcomingFilms() = networkBoundResource(
        query = { filmDao.getMovies("upcoming")},
        fetch = { api.getUpcomingMovies() },
        saveFetchResult = { response ->
            val films = response.body()?.results?.map { it.copy(mediaType = "movie", category = "upcoming") }
            films?.let {
                database.withTransaction {
                        filmDao.deleteMovies("upcoming")
                        filmDao.insertFilms(it)
                }
            }
        }
    )

    override fun getTrendingFilms(filmType: FilmType) = networkBoundResource(
        query = { if (filmType == FilmType.MOVIE) { filmDao.getMovies("trending") } else { filmDao.getTvShows("trending") } },
        fetch = { if (filmType == FilmType.MOVIE) { api.getTrendingMovies() } else { api.getTrendingSeries() } },
        saveFetchResult = { response ->
            val films = if (filmType == FilmType.MOVIE) {
                response.body()?.results?.map { it.copy(mediaType = "movie", category = "trending") }
            } else {
                response.body()?.results?.map { it.copy(mediaType = "tv", category = "trending") }
            }
            films?.let {
                database.withTransaction {
                    if (filmType == FilmType.MOVIE) {
                        filmDao.deleteMovies("trending")
                        filmDao.insertFilms(it)
                    } else {
                        filmDao.deleteTvShows("trending")
                        filmDao.insertFilms(it)
                    }
                }
            }
        }
    )

    override fun getOnAirFilms() = networkBoundResource(
        query = { filmDao.getTvShows("onAir")},
        fetch = { api.getOnAirSeries() },
        saveFetchResult = { response ->
            val films = response.body()?.results?.map { it.copy(mediaType = "tv", category = "onAir") }
            films?.let {
                database.withTransaction {
                        filmDao.deleteTvShows("onAir")
                        filmDao.insertFilms(it)
                }
            }
        }
    )

    override fun getTopRatedFilms(filmType: FilmType) = networkBoundResource(
        query = { if (filmType == FilmType.MOVIE) { filmDao.getMovies("topRated") } else { filmDao.getTvShows("topRated") } },
        fetch = { if (filmType == FilmType.MOVIE) { api.getTopRatedMovies() } else { api.getTopRatedSeries() } },
        saveFetchResult = { response ->
            val films = if (filmType == FilmType.MOVIE) {
                response.body()?.results?.map { it.copy(mediaType = "movie", category = "topRated") }
            } else {
                response.body()?.results?.map { it.copy(mediaType = "tv", category = "topRated") }
            }
            films?.let {
                database.withTransaction {
                    if (filmType == FilmType.MOVIE) {
                        filmDao.deleteMovies("topRated")
                        filmDao.insertFilms(it)
                    } else {
                        filmDao.deleteTvShows("topRated")
                        filmDao.insertFilms(it)
                    }
                }
            }
        }
    )

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