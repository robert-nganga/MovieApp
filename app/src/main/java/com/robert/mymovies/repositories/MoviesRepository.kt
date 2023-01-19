package com.robert.mymovies.repositories

import com.robert.mymovies.api.MoviesAPI
import com.robert.mymovies.data.remote.responses.CastResponse
import com.robert.mymovies.data.remote.responses.GenreResponse
import com.robert.mymovies.data.remote.MovieDetailsResponse
import com.robert.mymovies.data.remote.MovieResponse
import retrofit2.Response
import javax.inject.Inject

class MoviesRepository@Inject constructor(private val api: MoviesAPI): Repository {


    override suspend fun getPopularMovies(page: Int): Response<MovieResponse> {
        return api.getPopularMovies(page = page)
    }

    override suspend fun getUpcomingMovies(page: Int): Response<MovieResponse> {
        return api.getUpcomingMovies(page = page)
    }

    override suspend fun getSimilarMovies(movieId: Int): Response<MovieResponse> {
        return api.getSimilarMovies(filmId = movieId)
    }

    override suspend fun getMovieCredits(movieId: Int): Response<CastResponse> {
        return api.getMovieCredits(filmId = movieId)
    }

    override suspend fun getMovieDetails(movieId: Int): Response<MovieDetailsResponse> {
        return api.getMovieDetails(filmId = movieId)
    }

    override suspend fun getGenreList(): Response<GenreResponse> {
        return api.getGenreList()
    }

    override suspend fun getTrendingMovies(): Response<MovieResponse> {
        return api.getTrendingMovies()
    }
}