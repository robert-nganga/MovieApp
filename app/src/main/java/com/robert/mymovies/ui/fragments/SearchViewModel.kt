package com.robert.mymovies.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.mymovies.model.SearchResponse
import com.robert.mymovies.repositories.RepositorySearch
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel@Inject constructor(
        private val repository: RepositorySearch): ViewModel() {

    private var _searchResult: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()
    val searchResult: LiveData<Resource<SearchResponse>> get() = _searchResult

    var searchPage = 2

    private var searchResponse: SearchResponse? = null


    fun getSearchResults(query: String) = viewModelScope.launch {
        _searchResult.postValue(Resource(Resource.Status.LOADING, null, null))
        val result = repository.searchFilms(query = query, 1)
        searchResponse = result.data
        _searchResult.postValue(result)
    }


//    fun getPagedSearchResults(query: String) = viewModelScope.launch {
//        _searchResult.postValue(Resource(Resource.Status.LOADING, null, null))
//        val result = repository.searchFilms(query = query, page = searchPage)
//        if (result.status == Resource.Status.SUCCESS){
//            result.data?.let { resultResponse ->
//                searchPage++
//                if (searchResponse == null){
//                    searchResponse = resultResponse
//                }else{
//                    val oldMovies = searchResponse?.results
//                    val newMovies = resultResponse.results
//                    oldMovies?.addAll(newMovies)
//                }
//                _searchResult.postValue(Resource(Resource.Status.SUCCESS, searchResponse ?:resultResponse, null))
//            }
//        }else{
//            _searchResult.postValue(result)
//        }
//    }
}