package com.robert.mymovies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.robert.mymovies.model.Film
import com.robert.mymovies.model.Genre
import com.robert.mymovies.model.MovieDetails


@Database(entities = [Film::class, Genre::class, MovieDetails::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FilmDatabase: RoomDatabase() {

        abstract fun filmDao(): FilmDao
        abstract fun genreDao(): GenreDao
        abstract fun movieDetailsDao(): MovieDetailsDao
}