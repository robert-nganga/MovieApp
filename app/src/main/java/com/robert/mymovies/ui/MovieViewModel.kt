package com.robert.mymovies.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.mymovies.data.remote.Movie
import com.robert.mymovies.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    init {
        getPopularMovies()
    }

    private val _allMovies = MutableLiveData<List<Movie>>()
    val allMovies: LiveData<List<Movie>>
        get() = _allMovies


    private fun getPopularMovies() = viewModelScope.launch {
        val result = repository.getPopularMovies()
        if(result.isSuccessful){
            result.body()?.let {
                Log.i("ViewModel", it.results[0].title)
                _allMovies.value = it.results
            }
        }
    }

}