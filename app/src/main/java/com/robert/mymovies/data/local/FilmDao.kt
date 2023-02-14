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

        @Query("SELECT * FROM films WHERE category = :category AND mediaType = 'tv'")
        fun getTvShows(category: String): Flow<List<Film>>

        @Query("SELECT * FROM films WHERE category = :category AND mediaType = 'movie'")
        fun getMovies(category: String): Flow<List<Film>>

        @Query("DELETE FROM films WHERE category = :category AND mediaType = 'movie'")
        suspend fun deleteMovies(category: String)

        @Query("DELETE FROM films WHERE category = :category AND mediaType = 'tv'")
        suspend fun deleteTvShows(category: String)

        @Query("DELETE FROM films")
        suspend fun deleteAllFilms()

        @Query("SELECT * FROM films WHERE id = :id")
        suspend fun getFilm(id: Int): Film
}