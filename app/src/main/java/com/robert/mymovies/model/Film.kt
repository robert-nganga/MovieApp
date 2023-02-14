package com.robert.mymovies.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "films")
data class Film(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("genre_ids")
    val genres: List<Genre>?,
    @SerializedName("media_type")
    val mediaType: String?,
    @SerializedName("id")
    @PrimaryKey
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("release_date", alternate = ["first_air_date"])
    val releaseDate: String,
    @SerializedName("runtime")
    val runtime: Int?,
    @SerializedName("title", alternate = ["name"])
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    val category: String
)