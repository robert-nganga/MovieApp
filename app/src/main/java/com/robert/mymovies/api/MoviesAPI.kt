package com.robert.mymovies.api

import com.robert.mymovies.BuildConfig
import com.robert.mymovies.data.remote.CastResponse
import com.robert.mymovies.data.remote.GenreResponse
import com.robert.mymovies.data.remote.MovieDetailsResponse
import com.robert.mymovies.data.remote.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language")
        language: String = "en-US",
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("page")
        page: Int = 1
    ): Response<MovieResponse>

    @GET("3/movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id")
        filmId: Int,
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("language")
        language: String = "en-US",
    ): Response<CastResponse>

    @GET("3/movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id")
        filmId: Int,
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("language")
        language: String = "en-US",
    ): Response<MovieResponse>

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id")
        filmId: Int,
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("language")
        language: String = "en-US",
    ): Response<MovieDetailsResponse>
}