package com.robert.mymovies.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_details")
data class MovieDetails(
    val adult: Boolean,
    val backdrop_path: String,
    val genres: List<Genre>,
    val homepage: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val runtime: Int,
    val status: String,
    val tagline: String,
    val title: String,
    val vote_average: Double,
    val vote_count: Int,
    val timeStamp: String
)