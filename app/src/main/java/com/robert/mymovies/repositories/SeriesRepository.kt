package com.robert.mymovies.repositories

import com.robert.mymovies.api.MoviesAPI
import com.robert.mymovies.data.remote.CastResponse
import com.robert.mymovies.data.remote.GenreResponse
import com.robert.mymovies.data.remote.SeriesDetailsResponse
import com.robert.mymovies.data.remote.SeriesResponse
import retrofit2.Response
import javax.inject.Inject

class SeriesRepository@Inject constructor(private val api: MoviesAPI): RepositorySeries {
    override suspend fun getPopularSeries(page: Int): Response<SeriesResponse> {
        return api.getPopularSeries(page = page)
    }

    override suspend fun getOnAirSeries(page: Int): Response<SeriesResponse> {
        return api.getOnAirSeries(page = page)
    }

    override suspend fun getTopRatedSeries(page: Int): Response<SeriesResponse> {
        return api.getTopRatedSeries(page = page)
    }

    override suspend fun getSimilarSeries(seriesId: Int): Response<SeriesResponse> {
        return api.getSimilarSeries(filmId = seriesId)
    }

    override suspend fun getSeriesCredits(seriesId: Int): Response<CastResponse> {
        return api.getSeriesCredits(filmId = seriesId)
    }

    override suspend fun getSeriesDetails(seriesId: Int): Response<SeriesDetailsResponse> {
        return api.getSeriesDetails(filmId = seriesId)
    }

    override suspend fun getSeriesGenreList(): Response<GenreResponse> {
        return api.getSeriesGenreList()
    }
}