package com.robert.mymovies.repositories

import com.robert.mymovies.data.remote.responses.SearchResponse
import com.robert.mymovies.utils.Resource

interface RepositorySearch {
    suspend fun searchFilms(query: String, page: Int): Resource<SearchResponse>
}