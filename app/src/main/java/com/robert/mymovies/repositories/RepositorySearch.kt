package com.robert.mymovies.repositories

import com.robert.mymovies.model.SearchResponse
import com.robert.mymovies.utils.Resource

interface RepositorySearch {
    suspend fun searchFilms(query: String, page: Int): Resource<SearchResponse>
}