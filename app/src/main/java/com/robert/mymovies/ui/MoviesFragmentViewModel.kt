package com.robert.mymovies.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.mymovies.data.remote.Genre
import com.robert.mymovies.data.remote.GenreResponse
import com.robert.mymovies.data.remote.Movie
import com.robert.mymovies.data.remote.MovieResponse
import com.robert.mymovies.repositories.Repository
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.Duration
import javax.inject.Inject


@HiltViewModel
class MoviesFragmentViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _allPopularMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val allPopularMovies: LiveData<Resource<MovieResponse>>
        get() = _allPopularMovies

    private val _allUpcomingMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val allUpcomingMovies: LiveData<Resource<MovieResponse>>
        get() = _allUpcomingMovies

    private val _allTrendingMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val allTrendingMovies: LiveData<Resource<MovieResponse>>
        get() = _allTrendingMovies

    private val _allGenres: MutableLiveData<Resource<GenreResponse>> = MutableLiveData()
    val allGenres: LiveData<Resource<GenreResponse>>
        get() = _allGenres

    fun fetchData() = viewModelScope.launch {
        getGenreList()
        delay(100L)
        getTrendingMovies()
        delay(100L)
        getPopularMovies()
        delay(100L)
        getUpcomingMovies()
    }

    private fun handleResponse(response: Response<MovieResponse>): Resource<MovieResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource(Resource.Status.SUCCESS, resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }

    private fun getPopularMovies() = viewModelScope.launch {
        _allPopularMovies.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getPopularMovies(2)
        _allPopularMovies.postValue(handleResponse(result))

    }

    private fun getUpcomingMovies() = viewModelScope.launch {
        _allUpcomingMovies.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getUpcomingMovies(2)
        _allUpcomingMovies.postValue(handleResponse(result))

    }

    private fun getTrendingMovies() = viewModelScope.launch {
        _allTrendingMovies.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getTrendingMovies()
        _allTrendingMovies.postValue(handleResponse(result))
    }

    private fun getGenreList() = viewModelScope.launch {
       _allGenres.value = Resource(Resource.Status.LOADING, null, null)
        val result = repository.getGenreList()
        if (result.isSuccessful){
            result.body()?.let { response->
                _allGenres.postValue(Resource(Resource.Status.SUCCESS, response, null))
            }
        }else{
            _allGenres.postValue(Resource(Resource.Status.ERROR, null, result.message()))
        }
    }

    init {
        fetchData()
    }

}