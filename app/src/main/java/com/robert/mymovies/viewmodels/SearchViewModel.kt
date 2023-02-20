package com.robert.mymovies.viewmodels

import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.paging.filter
import com.robert.mymovies.data.remote.responses.SearchResponse
import com.robert.mymovies.repositories.SearchRepository
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel@Inject constructor(
        private val repository: SearchRepository): ViewModel() {

    private val query = MutableLiveData<String>()


    val searchFilms = query.switchMap {  query->
        repository.searchFilms(query).map { pagingData->
            pagingData.filter { search-> search.mediaType != "person"}
        }.cachedIn(viewModelScope).asLiveData()
    }



    private var searchTerm = ""
    fun getQuery(text: String){
        if (searchTerm == text)
            return
        searchTerm = text
        query.postValue(text)
    }

}