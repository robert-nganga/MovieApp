package com.robert.mymovies.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.robert.mymovies.data.paging.FilmPagingSource
import com.robert.mymovies.data.remote.MoviesAPI
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.model.Film
import com.robert.mymovies.utils.Constants.QUERY_PAGE_SIZE
import com.robert.mymovies.utils.FilmType
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class MoreFilmsRepositoryImpl@Inject constructor(private val api: MoviesAPI): MoreFilmsRepository {


    override fun getMoreFilms(filmType: FilmType, category: String): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(
                pageSize = QUERY_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { FilmPagingSource(api, filmType, category) }
        ).flow
    }


}
