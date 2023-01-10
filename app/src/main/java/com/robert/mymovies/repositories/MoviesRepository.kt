package com.robert.mymovies.repositories

import com.robert.mymovies.api.MoviesAPI
import com.robert.mymovies.data.remote.MovieResponse
import retrofit2.Response
import javax.inject.Inject

class MoviesRepository@Inject constructor(private val api: MoviesAPI): Repository {


    override suspend fun getPopularMovies(): Response<MovieResponse> {
        return api.getPopularMovies()
    }
}