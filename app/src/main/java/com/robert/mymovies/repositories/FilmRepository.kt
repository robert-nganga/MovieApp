package com.robert.mymovies.repositories

import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.data.remote.responses.GenreResponse
import com.robert.mymovies.model.Film
import com.robert.mymovies.utils.FilmType
import com.robert.mymovies.utils.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface FilmRepository {

    fun getPopularFilms(filmType: FilmType): Flow<Resource<List<Film>>>

    fun getUpcomingFilms(): Flow<Resource<List<Film>>>

    fun getTrendingFilms(filmType: FilmType): Flow<Resource<List<Film>>>

    fun getOnAirFilms(): Flow<Resource<List<Film>>>

    fun getTopRatedFilms(filmType: FilmType): Flow<Resource<List<Film>>>

    suspend fun getGenreList(filmType: FilmType): Resource<GenreResponse>
}