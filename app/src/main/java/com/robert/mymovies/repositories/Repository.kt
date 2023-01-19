package com.robert.mymovies.repositories

import com.robert.mymovies.data.remote.responses.CastResponse
import com.robert.mymovies.data.remote.responses.GenreResponse
import com.robert.mymovies.data.remote.MovieDetailsResponse
import com.robert.mymovies.data.remote.MovieResponse
import retrofit2.Response

interface Repository {

    suspend fun getPopularMovies(page: Int): Response<MovieResponse>

    suspend fun getUpcomingMovies(page: Int): Response<MovieResponse>

    suspend fun getSimilarMovies(movieId: Int): Response<MovieResponse>

    suspend fun getMovieCredits(movieId: Int): Response<CastResponse>

    suspend fun getMovieDetails(movieId: Int):Response<MovieDetailsResponse>

    suspend fun getGenreList(): Response<GenreResponse>

    suspend fun getTrendingMovies(): Response<MovieResponse>
}