package com.robert.mymovies.viewmodels

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.robert.mymovies.data.remote.responses.FilmResponse
import com.robert.mymovies.data.remote.responses.GenreResponse
import com.robert.mymovies.repositories.FilmRepository
import com.robert.mymovies.repositories.MoreFilmsRepository
import com.robert.mymovies.utils.FilmType
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MoreFilmsFragmentViewModel@Inject constructor(private val repository: MoreFilmsRepository): ViewModel() {

        fun getMoreFilms(filmType: FilmType, category: String) =
                repository.getMoreFilms(filmType, category).cachedIn(viewModelScope)


}

