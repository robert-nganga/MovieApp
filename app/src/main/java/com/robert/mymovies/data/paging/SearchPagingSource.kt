package com.robert.mymovies.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.robert.mymovies.data.remote.MoviesAPI
import com.robert.mymovies.model.Search

class SearchPagingSource(
    private val api: MoviesAPI,
    private val query: String
): PagingSource<Int, Search>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
            val position = params.key ?: 1
            return try {
                val response = api.searchFilms(query = query, page = position)
                val films = response.body()?.results ?: emptyList()
                LoadResult.Page(
                    data = films,
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = if (films.isEmpty()) null else position + 1
                )
            }catch (e: Exception){
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, Search>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }
}