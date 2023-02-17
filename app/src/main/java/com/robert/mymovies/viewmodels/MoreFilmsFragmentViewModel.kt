package com.robert.mymovies.viewmodels

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.robert.mymovies.repositories.MoreFilmsRepository
import com.robert.mymovies.utils.FilmType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MoreFilmsFragmentViewModel@Inject constructor(private val repository: MoreFilmsRepository): ViewModel() {

        private val details = MutableLiveData<Map<String, String>>()

        val films = details.switchMap { map ->
                val type = if (map["type"] == "Movie") FilmType.MOVIE else FilmType.TVSHOW
                repository.getMoreFilms(type, map["category"]!!).cachedIn(viewModelScope).asLiveData()
        }


        private var type = ""
        private var filmCategory = ""
        fun getDetails(filmType: String, category: String){
                if (type == filmType && filmCategory == category)
                        return
                type = filmType
                filmCategory = category
                details.value = mapOf("type" to filmType, "category" to category)

        }


}

