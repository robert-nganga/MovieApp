package com.robert.mymovies.data.remote.responses

import com.robert.mymovies.model.Search

data class SearchResponse(
    val page: Int,
    val results: MutableList<Search>,
    val total_pages: Int,
    val total_results: Int
)