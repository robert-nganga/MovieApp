package com.robert.mymovies.repositories

import com.robert.mymovies.data.remote.SeriesDetailsResponse
import com.robert.mymovies.data.remote.responses.CastResponse
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.utils.Resource

interface RepositorySeriesDetails {

    suspend fun getSeriesCastDetails(filmId: Int): Resource<CastResponse>

    suspend fun getSimilarSeries(filmId: Int): Resource<FilmResponse>

    suspend fun getSeriesDetails(filmId: Int): Resource<SeriesDetailsResponse>
}