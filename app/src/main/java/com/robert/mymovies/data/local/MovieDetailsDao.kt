package com.robert.mymovies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.robert.mymovies.model.MovieDetails
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieDetails)

    @Query("SELECT * FROM movie_details WHERE id = :id")
    fun getMovieById(id: Int): Flow<MovieDetails>

    @Query("DELETE FROM movie_details WHERE id = :id")
    suspend fun deleteMovie(id: Int)
}