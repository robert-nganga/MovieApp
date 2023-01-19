package com.robert.mymovies.data.remote.responses

import com.robert.mymovies.model.Film

data class FilmResponse(
    val page: Int,
    val results: MutableList<Film>,
    val total_pages: Int,
    val total_results: Int
)