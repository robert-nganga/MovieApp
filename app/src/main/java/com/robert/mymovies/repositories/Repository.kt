package com.robert.mymovies.repositories

import com.robert.mymovies.data.remote.GenreResponse
import com.robert.mymovies.data.remote.MovieResponse
import retrofit2.Response

interface Repository {

    suspend fun getPopularMovies(): Response<MovieResponse>

    suspend fun getUpcomingMovies(): Response<MovieResponse>

    suspend fun getGenreList(): Response<GenreResponse>

    suspend fun getTrendingMovies(): Response<MovieResponse>
}