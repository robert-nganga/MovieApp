package com.robert.mymovies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.robert.mymovies.model.Film


@Database(entities = [Film::class], version = 1, exportSchema = false)
abstract class FilmDatabase: RoomDatabase() {

        abstract fun filmDao(): FilmDao
}