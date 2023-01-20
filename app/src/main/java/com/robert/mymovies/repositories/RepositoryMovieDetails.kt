package com.robert.mymovies.repositories

import com.robert.mymovies.data.remote.MovieDetailsResponse
import com.robert.mymovies.data.remote.responses.CastResponse
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.utils.Resource

interface RepositoryMovieDetails {

    suspend fun getMovieDetails(filmId:Int): Resource<MovieDetailsResponse>

    suspend fun getMovieCast(filmId: Int): Resource<CastResponse>

    suspend fun getSimilarMovies(filmId: Int): Resource<FilmResponse>
}