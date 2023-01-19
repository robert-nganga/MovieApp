package com.robert.mymovies.data.remote.responses

import com.robert.mymovies.model.Cast

data class CastResponse(
    val cast: List<Cast>,
    val id: Int
)