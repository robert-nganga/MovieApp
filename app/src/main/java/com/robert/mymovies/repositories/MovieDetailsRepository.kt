package com.robert.mymovies.repositories

import com.robert.mymovies.model.MovieDetails
import com.robert.mymovies.data.remote.responses.CastResponse
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.utils.Resource

interface MovieDetailsRepository {

    suspend fun getMovieDetails(filmId:Int): Resource<MovieDetails>

    suspend fun getMovieCast(filmId: Int): Resource<CastResponse>

    suspend fun getSimilarMovies(filmId: Int): Resource<FilmResponse>
}