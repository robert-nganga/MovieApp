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
import java.time.Duration
import javax.inject.Inject


@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    init {
        fetchData()
    }

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


    fun getPopularMovies() = viewModelScope.launch {
        //_allPopularMovies.postValue(Resource.Loading())
        val result = repository.getPopularMovies()
        if(result.isSuccessful){
            result.body()?.let { response->
                _allPopularMovies.postValue(Resource.Success(response))
            }
        }else{
            _allPopularMovies.postValue(Resource.Error(result.message()))
        }

    }

    fun getUpcomingMovies() = viewModelScope.launch {
        //_allUpcomingMovies.postValue(Resource.Loading())
        val result = repository.getUpcomingMovies()
        if(result.isSuccessful){
            result.body()?.let { response->
                _allUpcomingMovies.postValue(Resource.Success(response))
            }
        }else{
            _allUpcomingMovies.postValue(Resource.Error(result.message()))
        }

    }

    private fun getTrendingMovies() = viewModelScope.launch {
        //_allTrendingMovies.postValue(Resource.Loading())
        val result = repository.getTrendingMovies()
        if(result.isSuccessful){
            result.body()?.let { response->
                _allTrendingMovies.postValue(Resource.Success(response))
            }
        }else{
            _allTrendingMovies.postValue(Resource.Error(result.message()))
        }
    }

    private fun getGenreList() = viewModelScope.launch {
       // _allGenres.postValue(Resource.Loading())
        val result = repository.getGenreList()
        if (result.isSuccessful){
            result.body()?.let { response->
                _allGenres.postValue(Resource.Success(response))
            }
        }else{
            _allGenres.postValue(Resource.Error(result.message()))
        }
    }

}