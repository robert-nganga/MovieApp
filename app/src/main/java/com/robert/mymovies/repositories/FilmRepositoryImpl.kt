package com.robert.mymovies.repositories

import androidx.room.withTransaction
import com.robert.mymovies.data.local.FilmDatabase
import com.robert.mymovies.data.remote.MoviesAPI
import com.robert.mymovies.model.Film
import com.robert.mymovies.utils.FilmType
import com.robert.mymovies.utils.networkBoundResource
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FilmRepositoryImpl@Inject constructor(
        private val database: FilmDatabase,
        private val api: MoviesAPI
): FilmRepository {

    private val filmDao = database.filmDao()
    private val genreDao = database.genreDao()

    private val time = Date().toString()



    private fun shouldFetchFromNetwork(films: List<Film>): Boolean {
        if (films.isEmpty()) return true
        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val lastFetchTime = dateFormat.parse(films[0].timeStamp)
        val difference = lastFetchTime?.let { (Date().time - lastFetchTime.time) / (1000 * 60 * 60 * 24) }
        return difference != null && difference >= 1
    }


    override fun getPopularFilms(filmType: FilmType) = networkBoundResource(
        query = { if (filmType == FilmType.MOVIE) { filmDao.getMovies("popular") } else { filmDao.getTvShows("popular") } },
        fetch = { if (filmType == FilmType.MOVIE) { api.getPopularMovies() } else { api.getPopularSeries() } },
        saveFetchResult = { response ->
            val films = if (filmType == FilmType.MOVIE) {
                response.body()?.results?.map { it.copy(mediaType = "movie", category = "popular", timeStamp = time) }
            } else {
                response.body()?.results?.map { it.copy(mediaType = "tv", category = "popular", timeStamp = time) }
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
        },
        shouldFetch = { films -> shouldFetchFromNetwork(films) }
    )

    override fun getUpcomingFilms() = networkBoundResource(
        query = { filmDao.getMovies("upcoming")},
        fetch = { api.getUpcomingMovies() },
        saveFetchResult = { response ->
            val films = response.body()?.results?.map { it.copy(mediaType = "movie", category = "upcoming", timeStamp = time) }
            films?.let {
                database.withTransaction {
                        filmDao.deleteMovies("upcoming")
                        filmDao.insertFilms(it)
                }
            }
        },
        shouldFetch = { films -> shouldFetchFromNetwork(films) }
    )

    override fun getTrendingFilms(filmType: FilmType) = networkBoundResource(
        query = { if (filmType == FilmType.MOVIE) { filmDao.getMovies("trending") } else { filmDao.getTvShows("trending") } },
        fetch = { if (filmType == FilmType.MOVIE) { api.getTrendingMovies() } else { api.getTrendingSeries() } },
        saveFetchResult = { response ->
            val films = if (filmType == FilmType.MOVIE) {
                response.body()?.results?.map { it.copy(mediaType = "movie", category = "trending", timeStamp = time) }
            } else {
                response.body()?.results?.map { it.copy(mediaType = "tv", category = "trending", timeStamp = time) }
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
        },
        shouldFetch = { films -> shouldFetchFromNetwork(films) }
    )

    override fun getOnAirFilms() = networkBoundResource(
        query = { filmDao.getTvShows("onAir")},
        fetch = { api.getOnAirSeries() },
        saveFetchResult = { response ->
            val films = response.body()?.results?.map { it.copy(mediaType = "tv", category = "onAir", timeStamp = time) }
            films?.let {
                database.withTransaction {
                        filmDao.deleteTvShows("onAir")
                        filmDao.insertFilms(it)
                }
            }
        },
        shouldFetch = { films -> shouldFetchFromNetwork(films) }
    )

    override fun getTopRatedFilms(filmType: FilmType) = networkBoundResource(
        query = { if (filmType == FilmType.MOVIE) { filmDao.getMovies("topRated") } else { filmDao.getTvShows("topRated") } },
        fetch = { if (filmType == FilmType.MOVIE) { api.getTopRatedMovies() } else { api.getTopRatedSeries() } },
        saveFetchResult = { response ->
            val films = if (filmType == FilmType.MOVIE) {
                response.body()?.results?.map { it.copy(mediaType = "movie", category = "topRated", timeStamp = time) }
            } else {
                response.body()?.results?.map { it.copy(mediaType = "tv", category = "topRated", timeStamp = time) }
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
        },
        shouldFetch = { films -> shouldFetchFromNetwork(films) }
    )

    override fun getGenreList(filmType: FilmType) = networkBoundResource(
        query = { if (filmType == FilmType.MOVIE) { genreDao.getGenres("movie") } else { genreDao.getGenres("tv") } },
        fetch = { if (filmType == FilmType.MOVIE) { api.getMoviesGenreList() } else { api.getSeriesGenreList() } },
        saveFetchResult = { response ->
            val films = if (filmType == FilmType.MOVIE) {
                response.body()?.genres?.map { it.copy(mediaType = "movie", timeStamp = time) }
            } else {
                response.body()?.genres?.map { it.copy(mediaType = "tv", timeStamp = time) }
            }
            films?.let {
                database.withTransaction {
                    if (filmType == FilmType.MOVIE) {
                        genreDao.deleteMovieGenres("movie")
                        genreDao.insertGenres(it)
                    } else {
                        genreDao.deleteMovieGenres("tv")
                        genreDao.insertGenres(it)
                    }
                }
            }
        },
        shouldFetch = { true }
    )
}