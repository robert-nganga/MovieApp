package com.robert.mymovies.repositories

import com.robert.mymovies.data.remote.*
import retrofit2.Response

interface RepositorySeries {

    suspend fun getPopularSeries(page: Int): Response<SeriesResponse>

    suspend fun getOnAirSeries(page: Int): Response<SeriesResponse>

    suspend fun getTopRatedSeries(page: Int): Response<SeriesResponse>

    suspend fun getSimilarSeries(seriesId: Int): Response<SeriesResponse>

    suspend fun getSeriesCredits(seriesId: Int): Response<CastResponse>

    suspend fun getSeriesDetails(seriesId: Int): Response<SeriesDetailsResponse>

    suspend fun getSeriesGenreList(): Response<GenreResponse>
}