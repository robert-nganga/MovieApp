package com.robert.mymovies.viewmodels

import androidx.lifecycle.*
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.data.remote.responses.GenreResponse
import com.robert.mymovies.repositories.FilmRepository
import com.robert.mymovies.utils.FilmType
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class MoreFilmsFragmentViewModel@Inject constructor(
        private val repository: FilmRepository): ViewModel() {
/*
    private val _allFilms: MutableLiveData<Resource<FilmResponse>> = MutableLiveData()
    val allFilms: LiveData<Resource<FilmResponse>>
        get() = _allFilms
    var allFilmsPage = 1

    private var allFilmsResponse: FilmResponse? = null

    private val _genres: MutableLiveData<Resource<GenreResponse>> = MutableLiveData()
    val genres: LiveData<Resource<GenreResponse>> get() = _genres



    fun getGenres(filmType: FilmType) = viewModelScope.launch {
        _genres.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getGenreList(filmType)
        _genres.postValue(result)
    }

    fun getPopularFilms(filmType: FilmType) = viewModelScope.launch {
        _allFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getPopularFilms(allFilmsPage, filmType)
        if (result.status == Resource.Status.SUCCESS){
            result.data?.let { resultResponse ->
                allFilmsPage++
                if (allFilmsResponse == null){
                    allFilmsResponse = resultResponse
                }else{
                    val oldMovies = allFilmsResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                _allFilms.postValue(Resource(Resource.Status.SUCCESS, allFilmsResponse ?:resultResponse, null))
            }
        }else{
            _allFilms.postValue(result)
        }
    }

    fun getOnAirFilms(filmType: FilmType) = viewModelScope.launch {
        _allFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getOnAirFilms(allFilmsPage, filmType)
        if (result.status == Resource.Status.SUCCESS){
            result.data?.let { resultResponse ->
                allFilmsPage++
                if (allFilmsResponse == null){
                    allFilmsResponse = resultResponse
                }else{
                    val oldMovies = allFilmsResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                _allFilms.postValue(Resource(Resource.Status.SUCCESS, allFilmsResponse ?:resultResponse, null))
            }
        }else{
            _allFilms.postValue(result)
        }
    }

    fun getUpcomingFilms(filmType: FilmType) = viewModelScope.launch {
        _allFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getUpcomingFilms(allFilmsPage, filmType)
        if (result.status == Resource.Status.SUCCESS){
            result.data?.let { resultResponse ->
                allFilmsPage++
                if (allFilmsResponse == null){
                    allFilmsResponse = resultResponse
                }else{
                    val oldMovies = allFilmsResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                _allFilms.postValue(Resource(Resource.Status.SUCCESS, allFilmsResponse ?:resultResponse, null))
            }
        }else{
            _allFilms.postValue(result)
        }
    }

    fun getTopRatedFilms(filmType: FilmType) = viewModelScope.launch {
        _allFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getTopRatedFilms(allFilmsPage, filmType)
        if (result.status == Resource.Status.SUCCESS){
            result.data?.let { resultResponse ->
                allFilmsPage++
                if (allFilmsResponse == null){
                    allFilmsResponse = resultResponse
                }else{
                    val oldMovies = allFilmsResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                _allFilms.postValue(Resource(Resource.Status.SUCCESS, allFilmsResponse ?:resultResponse, null))
            }
        }else{
            _allFilms.postValue(result)
        }
    }

 */
}

