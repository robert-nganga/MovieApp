package com.robert.mymovies.model

data class SearchResponse(
    val page: Int,
    val results: List<Search>,
    val total_pages: Int,
    val total_results: Int
)