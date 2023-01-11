package com.robert.mymovies.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.mymovies.data.remote.Genre
import com.robert.mymovies.data.remote.Movie
import com.robert.mymovies.repositories.Repository
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

    private fun fetchData() = viewModelScope.launch {
        getGenreList()
        delay(100L)
        getTrendingMovies()
        delay(100L)
        getPopularMovies()
    }

    private val _allPopularMovies = MutableLiveData<List<Movie>>()
    val allPopularMovies: LiveData<List<Movie>>
        get() = _allPopularMovies

    private val _allTrendingMovies = MutableLiveData<List<Movie>>()
    val allTrendingMovies: LiveData<List<Movie>>
        get() = _allTrendingMovies

    private val _allGenres = MutableLiveData<List<Genre>>()
    val allGenres: LiveData<List<Genre>>
        get() = _allGenres


    fun getPopularMovies() = viewModelScope.launch {
        val result = repository.getPopularMovies()
        if(result.isSuccessful){
            result.body()?.let {
                _allPopularMovies.value = it.results
            }
        }
    }

    private fun getTrendingMovies() = viewModelScope.launch {
        val result = repository.getTrendingMovies()
        if(result.isSuccessful){
            result.body()?.let {
                _allTrendingMovies.value = it.results
            }
        }
    }

    private fun getGenreList() = viewModelScope.launch {
        val result = repository.getGenreList()
        if (result.isSuccessful){
            result.body()?.let {
                _allGenres.value = it.genres
            }
        }
    }

}