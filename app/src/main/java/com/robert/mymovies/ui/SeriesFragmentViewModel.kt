package com.robert.mymovies.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SeriesFragmentViewModel@Inject constructor(app: Application): AndroidViewModel(app) {

}