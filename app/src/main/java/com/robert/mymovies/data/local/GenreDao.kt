package com.robert.mymovies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.robert.mymovies.model.Genre
import kotlinx.coroutines.flow.Flow


@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<Genre>)

    @Query("SELECT * FROM genres WHERE mediaType = 'tv'")
    fun getTvShows(mediaType: String): Flow<List<Genre>>

    @Query("DELETE FROM genres WHERE mediaType = :mediaType")
    suspend fun deleteMovieGenres(mediaType: String)
}