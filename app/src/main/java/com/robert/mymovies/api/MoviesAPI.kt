package com.robert.mymovies.api

import com.robert.mymovies.BuildConfig
import com.robert.mymovies.data.remote.GenreResponse
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

    @GET("3/genre/movie/list")
    suspend fun getGenreList(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("language")
        language: String = "en-US",
    ):Response<GenreResponse>

    @GET("3/trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
    ): Response<MovieResponse>
}