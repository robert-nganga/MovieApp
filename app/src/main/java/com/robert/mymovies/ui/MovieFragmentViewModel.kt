package com.robert.mymovies.ui

import androidx.lifecycle.ViewModel
import com.robert.mymovies.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MovieFragmentViewModel@Inject constructor(private val repository: Repository):ViewModel() {


}