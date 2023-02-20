package com.robert.mymovies.repositories

import androidx.paging.PagingData
import com.robert.mymovies.data.remote.responses.SearchResponse
import com.robert.mymovies.model.Search
import com.robert.mymovies.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchFilms(query: String): Flow<PagingData<Search>>
}