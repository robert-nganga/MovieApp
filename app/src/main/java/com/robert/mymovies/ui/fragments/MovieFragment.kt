package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.robert.mymovies.R
import com.robert.mymovies.ui.MovieFragmentViewModel
import com.robert.mymovies.utils.Constants
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFragment: Fragment(R.layout.fragment_movie) {

    private val viewModel: MovieFragmentViewModel by viewModels()
    private lateinit var collapsingToolBar: CollapsingToolbarLayout
    private lateinit var imgToolBar: ImageView
    val args: MovieFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collapsingToolBar = view.findViewById(R.id.collapsingToolBar)
        imgToolBar = view.findViewById(R.id.imgToolBar)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        viewModel.getMovieDetails(args.id)

        viewModel.movieDetails.observe(viewLifecycleOwner){ response->
            when(response.status){
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        val imageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${it.backdrop_path}"
                        Glide.with(view).load(imageUrl).into(imgToolBar)
                        collapsingToolBar.title = it.original_title
                        tvDescription.text = it.overview
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {}
            }
        }
    }
}