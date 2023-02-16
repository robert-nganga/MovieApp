package com.robert.mymovies.repositories

import androidx.paging.PagingData
import com.robert.mymovies.model.Film
import com.robert.mymovies.utils.FilmType
import kotlinx.coroutines.flow.Flow

interface MoreFilmsRepository {
    fun getMoreFilms(filmType:FilmType, category: String): Flow<PagingData<Film>>
}