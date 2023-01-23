package com.robert.mymovies.api

import com.robert.mymovies.BuildConfig
import com.robert.mymovies.data.remote.*
import com.robert.mymovies.data.remote.responses.CastResponse
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.data.remote.responses.GenreResponse
import com.robert.mymovies.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {

    @GET("3/search/multi")
    suspend fun searchFilms(
        @Query("language")
        language: String = "en-US",
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("page")
        page: Int = 1,
        @Query("query")
        query: String = "",
        @Query("include_adult")
        adult: Boolean = true
    ): Response<SearchResponse>

    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("language")
        language: String = "en-US",
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("page")
        page: Int = 1
    ): Response<FilmResponse>

    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language")
        language: String = "en-US",
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("page")
        page: Int = 1
    ): Response<FilmResponse>

    @GET("3/movie/latest")
    suspend fun getLatestMovies(
        @Query("language")
        language: String = "en-US",
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
    ): Response<FilmResponse>

    @GET("3/genre/movie/list")
    suspend fun getMoviesGenreList(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("language")
        language: String = "en-US",
    ):Response<GenreResponse>

    @GET("3/trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
    ): Response<FilmResponse>

    @GET("3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language")
        language: String = "en-US",
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("page")
        page: Int = 1
    ): Response<FilmResponse>

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
    ): Response<FilmResponse>

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id")
        filmId: Int,
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("language")
        language: String = "en-US",
    ): Response<MovieDetailsResponse>


    // Tv Shows

    @GET("3/tv/popular")
    suspend fun getPopularSeries(
        @Query("language")
        language: String = "en-US",
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("page")
        page: Int = 1
    ): Response<FilmResponse>

    @GET("3/tv/latest")
    suspend fun getLatestSeries(
        @Query("language")
        language: String = "en-US",
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
    ): Response<FilmResponse>

    @GET("3/genre/tv/list")
    suspend fun getSeriesGenreList(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("language")
        language: String = "en-US",
    ):Response<GenreResponse>

    @GET("3/tv/on_the_air")
    suspend fun getOnAirSeries(
        @Query("language")
        language: String = "en-US",
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("page")
        page: Int = 1
    ): Response<FilmResponse>

    @GET("3/tv/top_rated")
    suspend fun getTopRatedSeries(
        @Query("language")
        language: String = "en-US",
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("page")
        page: Int = 1
    ): Response<FilmResponse>

    @GET("3/trending/tv/day")
    suspend fun getTrendingSeries(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
    ): Response<FilmResponse>

    @GET("3/tv/{tv_id}/credits")
    suspend fun getSeriesCredits(
        @Path("tv_id")
        filmId: Int,
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("language")
        language: String = "en-US",
    ): Response<CastResponse>

    @GET("3/tv/{tv_id}/similar")
    suspend fun getSimilarSeries(
        @Path("tv_id")
        filmId: Int,
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("language")
        language: String = "en-US",
    ): Response<FilmResponse>

    @GET("3/tv/{tv_id}")
    suspend fun getSeriesDetails(
        @Path("tv_id")
        filmId: Int,
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("language")
        language: String = "en-US",
    ): Response<SeriesDetailsResponse>
}