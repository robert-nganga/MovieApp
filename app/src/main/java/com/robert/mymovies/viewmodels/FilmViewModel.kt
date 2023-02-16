package com.robert.mymovies.viewmodels

import androidx.lifecycle.*
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.data.remote.responses.GenreResponse
import com.robert.mymovies.model.Film
import com.robert.mymovies.model.Genre
import com.robert.mymovies.repositories.FilmRepository
import com.robert.mymovies.repositories.FilmRepositoryImpl
import com.robert.mymovies.utils.FilmType
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FilmViewModel@Inject constructor(
        private val repository: FilmRepository): ViewModel() {

    private var _allPopularFilms: MutableLiveData<Resource<List<Film>>> = MutableLiveData()
    val allPopularFilms: LiveData<Resource<List<Film>>> get() =  _allPopularFilms

    private var _allUpcomingFilms: MutableLiveData<Resource<List<Film>>> = MutableLiveData()
    val allUpcomingFilms: LiveData<Resource<List<Film>>> get() =  _allUpcomingFilms

    private var _allTrendingFilms: MutableLiveData<Resource<List<Film>>> = MutableLiveData()
    val allTrendingFilms: LiveData<Resource<List<Film>>> get() =  _allTrendingFilms

    private var _allTopRatedFilms: MutableLiveData<Resource<List<Film>>> = MutableLiveData()
    val allTopRatedFilms: LiveData<Resource<List<Film>>> get() =  _allTopRatedFilms

    private var _allOnAirFilms: MutableLiveData<Resource<List<Film>>> = MutableLiveData()
    val allOnAirFilms: LiveData<Resource<List<Film>>> get() =  _allOnAirFilms


    private var _allGenres: MutableLiveData<Resource<List<Genre>>> = MutableLiveData()
    val allGenres: LiveData<Resource<List<Genre>>> get() =  _allGenres


    fun fetchSeriesData(filmType: FilmType){
        getTrendingFilms(filmType)
        //getLatestFilms(filmType)
        getTopRated(filmType)
        getPopularFilms(filmType)
        getOnAirFilms()
    }

    fun fetchMovieData(filmType: FilmType){
        getTrendingFilms(filmType)
        //getLatestFilms(filmType)
        getPopularFilms(filmType)
        getUpcomingFilms()
        getTopRated(filmType)
    }

//    fun getId(position: Int): Int?{
//        return _allTrendingFilms.value?.data?.results?.let { it[position].id }
//    }

//    private fun getTrending(filmType: FilmType) {
//        _trending.value = repository.getTrending(filmType).asLiveData().value
//    }

    private fun getPopularFilms(filmType: FilmType) = viewModelScope.launch {
        repository.getPopularFilms( filmType).collect{
            _allPopularFilms.value = it
        }
    }

    private fun getUpcomingFilms() = viewModelScope.launch {
        repository.getUpcomingFilms().collect{
            _allUpcomingFilms.value = it
        }
    }

    private fun getTrendingFilms(filmType: FilmType) = viewModelScope.launch {
        repository.getTrendingFilms(filmType).collect{
            _allTrendingFilms.postValue(it)
        }
    }

    private fun getTopRated(filmType: FilmType) = viewModelScope.launch {
        repository.getTopRatedFilms(filmType).collect{
            _allTopRatedFilms.value = it
        }
    }

    private fun getOnAirFilms() = viewModelScope.launch {
        repository.getOnAirFilms().collect{
            _allOnAirFilms.value = it
        }
    }


    private fun getGenres(filmType: FilmType) = viewModelScope.launch {
        repository.getGenreList(filmType).collect{
            _allGenres.value = it
        }
    }
}