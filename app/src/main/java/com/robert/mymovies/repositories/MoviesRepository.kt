package com.robert.mymovies.repositories

import com.robert.mymovies.api.MoviesAPI
import com.robert.mymovies.data.remote.GenreResponse
import com.robert.mymovies.data.remote.MovieResponse
import retrofit2.Response
import javax.inject.Inject

class MoviesRepository@Inject constructor(private val api: MoviesAPI): Repository {


    override suspend fun getPopularMovies(): Response<MovieResponse> {
        return api.getPopularMovies()
    }

    override suspend fun getGenreList(): Response<GenreResponse> {
        return api.getGenreList()
    }

    override suspend fun getTrendingMovies(): Response<MovieResponse> {
        return api.getTrendingMovies()
    }
}