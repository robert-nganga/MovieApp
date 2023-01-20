package com.robert.mymovies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.data.remote.responses.GenreResponse
import com.robert.mymovies.repositories.RepositoryFilm
import com.robert.mymovies.utils.FilmType
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FilmViewModel@Inject constructor(private val repository: RepositoryFilm): ViewModel() {

    private var _allPopularFilms: MutableLiveData<Resource<FilmResponse>> = MutableLiveData()
    val allPopularFilms: LiveData<Resource<FilmResponse>> get() =  _allPopularFilms

    private var _allUpcomingFilms: MutableLiveData<Resource<FilmResponse>> = MutableLiveData()
    val allUpcomingFilms: LiveData<Resource<FilmResponse>> get() =  _allUpcomingFilms

    private var _allTrendingFilms: MutableLiveData<Resource<FilmResponse>> = MutableLiveData()
    val allTrendingFilms: LiveData<Resource<FilmResponse>> get() =  _allTrendingFilms

    private var _allTopRatedFilms: MutableLiveData<Resource<FilmResponse>> = MutableLiveData()
    val allTopRatedFilms: LiveData<Resource<FilmResponse>> get() =  _allTopRatedFilms

    private var _allOnAirFilms: MutableLiveData<Resource<FilmResponse>> = MutableLiveData()
    val allOnAirFilms: LiveData<Resource<FilmResponse>> get() =  _allOnAirFilms

    private var _allLatestFilms: MutableLiveData<Resource<FilmResponse>> = MutableLiveData()
    val allLatestFilms: LiveData<Resource<FilmResponse>> get() =  _allLatestFilms

    private var _allGenres: MutableLiveData<Resource<GenreResponse>> = MutableLiveData()
    val allGenres: LiveData<Resource<GenreResponse>> get() =  _allGenres


    fun fetchSeriesData(filmType: FilmType){
        getTopRated(filmType)
        getPopularFilms(filmType)
        getOnAirFilms(filmType)
    }

    fun fetchMovieData(filmType: FilmType){
        getPopularFilms(filmType)
        getUpcomingFilms(filmType)
        getTrendingFilms(filmType)
    }


    private fun getPopularFilms(filmType: FilmType) = viewModelScope.launch {
        _allPopularFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getPopularFilms(1, filmType)
        _allPopularFilms.postValue(result)
    }

    private fun getUpcomingFilms(filmType: FilmType) = viewModelScope.launch {
        _allUpcomingFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getUpcomingFilms(1, filmType)
        _allUpcomingFilms.postValue(result)
    }

    private fun getTrendingFilms(filmType: FilmType) = viewModelScope.launch {
        _allTrendingFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getTrendingFilms( filmType)
        _allTrendingFilms.postValue(result)
    }

    private fun getTopRated(filmType: FilmType) = viewModelScope.launch {
        _allTopRatedFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getTopRatedFilms(1, filmType)
        _allTopRatedFilms.postValue(result)
    }

    private fun getOnAirFilms(filmType: FilmType) = viewModelScope.launch {
        _allOnAirFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getOnAirFilms(1, filmType)
        _allOnAirFilms.postValue(result)
    }

    private fun getLatestFilms(filmType: FilmType) = viewModelScope.launch {
        _allLatestFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getLatestFilms(filmType)
        _allLatestFilms.postValue(result)
    }

    private fun getGenres(filmType: FilmType) = viewModelScope.launch {
        _allGenres.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getGenreList(filmType)
        _allGenres.postValue(result)
    }
}