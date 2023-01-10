package com.robert.mymovies.repositories

import com.robert.mymovies.data.remote.MovieResponse
import retrofit2.Response

interface Repository {

    suspend fun getPopularMovies(): Response<MovieResponse>
}