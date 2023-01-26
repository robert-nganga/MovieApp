package com.robert.mymovies.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.mymovies.data.remote.responses.SeriesDetailsResponse
import com.robert.mymovies.data.remote.responses.CastResponse
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.repositories.RepositorySeriesDetails
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesDetailsViewModel@Inject constructor(private val repository: RepositorySeriesDetails): ViewModel() {

    var seriesId: Int? = null

    private val _similarSeries: MutableLiveData<Resource<FilmResponse>> = MutableLiveData()
    val similarSeries: LiveData<Resource<FilmResponse>> get() =  _similarSeries

    private var _seriesDetails: MutableLiveData<Resource<SeriesDetailsResponse>> = MutableLiveData()
    val seriesDetails: LiveData<Resource<SeriesDetailsResponse>> get() = _seriesDetails

    private var _seriesCast: MutableLiveData<Resource<CastResponse>> = MutableLiveData()
    val seriesCast: LiveData<Resource<CastResponse>> get() =  _seriesCast

    fun fetchData(filmId: Int){
        getSeriesDetails(filmId)
        getSeriesCast(filmId)
        getSimilarSeries(filmId)
    }

    fun getSeriesDetails(filmId: Int) = viewModelScope.launch {
        _seriesDetails.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getSeriesDetails(filmId = filmId)
        _seriesDetails.postValue(result)
    }

    fun getSeriesCast(filmId: Int) = viewModelScope.launch {
        _seriesCast.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getSeriesCastDetails(filmId = filmId)
        _seriesCast.postValue(result)
    }

    fun getSimilarSeries(filmId: Int) = viewModelScope.launch {
        _similarSeries.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getSimilarSeries(filmId = filmId)
        _similarSeries.postValue(result)
    }


}