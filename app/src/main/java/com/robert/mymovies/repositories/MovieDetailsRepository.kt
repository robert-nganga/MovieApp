package com.robert.mymovies.repositories

import com.robert.mymovies.model.MovieDetails
import com.robert.mymovies.data.remote.responses.CastResponse
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieDetailsRepository {

    fun getMovieDetails(filmId:Int): Flow<Resource<MovieDetails>>

    suspend fun getMovieCast(filmId: Int): Resource<CastResponse>

    suspend fun getSimilarMovies(filmId: Int): Resource<FilmResponse>
}