package com.robert.mymovies.repositories

import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.data.remote.responses.GenreResponse
import com.robert.mymovies.utils.FilmType
import com.robert.mymovies.utils.Resource
import retrofit2.Response

interface RepositoryFilm {

    suspend fun getPopularFilms(page: Int, filmType: FilmType): Resource<FilmResponse>

    suspend fun getUpcomingFilms(page: Int, filmType: FilmType): Resource<FilmResponse>

    suspend fun getTrendingFilms(filmType: FilmType): Resource<FilmResponse>

    suspend fun getOnAirFilms(page: Int, filmType: FilmType): Resource<FilmResponse>

    suspend fun getTopRatedFilms(page: Int, filmType: FilmType): Resource<FilmResponse>

    suspend fun getLatestFilms(filmType: FilmType): Resource<FilmResponse>

    suspend fun getGenreList(filmType: FilmType): Resource<GenreResponse>
}