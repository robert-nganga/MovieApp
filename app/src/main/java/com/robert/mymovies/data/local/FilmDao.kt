package com.robert.mymovies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.robert.mymovies.model.Film
import kotlinx.coroutines.flow.Flow


@Dao
interface FilmDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertFilms(films: List<Film>)

        @Query("SELECT * FROM films WHERE category = :category")
        fun getFilms(category: String): Flow<List<Film>>

        @Query("DELETE FROM films WHERE category = :category")
        suspend fun deleteFilms(category: String)

        @Query("DELETE FROM films")
        suspend fun deleteAllFilms()

        @Query("SELECT * FROM films WHERE id = :id")
        suspend fun getFilm(id: Int): Film
}