package com.robert.mymovies.api

import com.robert.mymovies.BuildConfig
import com.robert.mymovies.data.remote.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesAPI {

    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("language")
        language: String = "en-US",
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("page")
        page: Int = 1
    ): Response<MovieResponse>
}