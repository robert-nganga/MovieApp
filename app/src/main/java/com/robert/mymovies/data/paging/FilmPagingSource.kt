package com.robert.mymovies.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.robert.mymovies.data.remote.MoviesAPI
import com.robert.mymovies.model.Film
import com.robert.mymovies.utils.FilmType

class FilmPagingSource(
        private val api: MoviesAPI,
        private val filmType: FilmType,
        private val category: String
): PagingSource<Int, Film>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        val page = params.key ?: 1
        return try {
            val response = when(filmType){
                FilmType.MOVIE -> {
                    when(category){
                        "popular" -> { Log.i("FilmPagingSource", "popular movies")
                            api.getPopularMovies(page = page).body() }
                        "topRated" -> { Log.i("FilmPagingSource", "topRated movies")
                            api.getTopRatedMovies(page = page).body() }
                        else -> { Log.i("FilmPagingSource", "upcoming movies")
                            api.getUpcomingMovies(page = page).body() }
                    }
                }
                FilmType.TVSHOW -> {
                    when(category){
                        "popular" -> { api.getPopularSeries(page = page).body() }
                        "topRated" -> { api.getTopRatedSeries(page = page).body() }
                        else -> { api.getOnAirSeries(page = page).body() }

                    }
                }
            }
            val films = response?.results ?: emptyList()
            val nextPage = if (films.isEmpty()) null else page + 1
            val prevPage = if (page == 1) null else page - 1
            Log.i("FilmPagingSource", "Loaded ${films.size} films page: $page next: $nextPage prev: $prevPage")
            LoadResult.Page(
                data = films,
                prevKey = prevPage,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, Film>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}