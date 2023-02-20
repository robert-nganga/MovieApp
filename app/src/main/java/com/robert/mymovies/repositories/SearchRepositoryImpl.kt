package com.robert.mymovies.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.paging.Pager
import androidx.paging.PagingData
import com.robert.mymovies.data.paging.SearchPagingSource
import com.robert.mymovies.data.remote.MoviesAPI
import com.robert.mymovies.data.remote.responses.SearchResponse
import com.robert.mymovies.model.Search
import com.robert.mymovies.utils.Constants
import com.robert.mymovies.utils.Resource
import kotlinx.coroutines.flow.Flow
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

class SearchRepositoryImpl@Inject constructor(
        private val app: Application,
        private val api: MoviesAPI
): SearchRepository {


    override fun searchFilms(query: String): Flow<PagingData<Search>> {
        return Pager(
            config = androidx.paging.PagingConfig(
                pageSize = Constants.QUERY_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchPagingSource(api, query) }
        ).flow
    }


}