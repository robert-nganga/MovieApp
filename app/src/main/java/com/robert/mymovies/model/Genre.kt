package com.robert.mymovies.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "genres")
data class Genre(
    val mediaType: String,
    @PrimaryKey
    val id: Int,
    val name: String,
    val timeStamp: String
)