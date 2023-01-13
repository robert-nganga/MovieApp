package com.robert.mymovies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.mymovies.data.remote.MovieResponse
import com.robert.mymovies.repositories.Repository
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MoreFilmsFragmentViewModel@Inject constructor(private val repository: Repository): ViewModel() {

    private val _allFilms: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val allFilms: LiveData<Resource<MovieResponse>>
        get() = _allFilms
    var allFilmsPage = 1
    private var allFilmsResponse: MovieResponse? = null

    private fun handleResponse(response: Response<MovieResponse>): Resource<MovieResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                allFilmsPage++
                if (allFilmsResponse == null){
                    allFilmsResponse = resultResponse
                }else{
                    val oldMovies = allFilmsResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                return  Resource(Resource.Status.SUCCESS, allFilmsResponse ?:resultResponse, null)
            }
        }
        return Resource(Resource.Status.ERROR, null, response.message())
    }


    fun getPopularMovies() = viewModelScope.launch {
        _allFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getPopularMovies(allFilmsPage)
        _allFilms.postValue(handleResponse(result))

    }


    fun getUpcomingMovies() = viewModelScope.launch {
        _allFilms.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.getUpcomingMovies(allFilmsPage)
        _allFilms.postValue(handleResponse(result))
    }
}