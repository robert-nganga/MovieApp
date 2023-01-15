package com.robert.mymovies.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.robert.mymovies.R
import com.robert.mymovies.adapters.GenresAdapter
import com.robert.mymovies.data.remote.MovieDetailsResponse
import com.robert.mymovies.ui.MovieFragmentViewModel
import com.robert.mymovies.utils.Constants
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFragment: Fragment(R.layout.fragment_movie) {

    private val viewModel: MovieFragmentViewModel by viewModels()
    private lateinit var collapsingToolBar: CollapsingToolbarLayout
    private lateinit var imgToolBar: ImageView
    private lateinit var imgPoster: ImageView
    private lateinit var tvTagLine: TextView
    private lateinit var tvYearPublished: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvBudget: TextView
    private lateinit var tvDescription: TextView
    private lateinit var rvMovieGenres: RecyclerView
    private lateinit var genresAdapter: GenresAdapter
    private val args: MovieFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collapsingToolBar = view.findViewById(R.id.collapsingToolBar)
        imgToolBar = view.findViewById(R.id.imgToolBar)
        imgPoster = view.findViewById(R.id.imgPoster)
        tvDescription = view.findViewById(R.id.tvDescription)
        tvTagLine = view.findViewById(R.id.tvTagLine)
        tvYearPublished = view.findViewById(R.id.tvYearPublished)
        tvRating = view.findViewById(R.id.tvRating)
        tvBudget = view.findViewById(R.id.tvBudget)
        rvMovieGenres = view.findViewById(R.id.rvMovieGenres)
        setupGenresRecyclerView()
        viewModel.getMovieDetails(args.id)

        viewModel.movieDetails.observe(viewLifecycleOwner){ response->
            when(response.status){
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        val backDropImageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${it.backdrop_path}"
                        val posterImageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${it.poster_path}"
                        Glide.with(view).load(backDropImageUrl).into(imgToolBar)
                        Glide.with(view).load(posterImageUrl).into(imgPoster)
                        setMovieDetails(it)
                        genresAdapter.updateList(it.genres)
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {}
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setMovieDetails(movie: MovieDetailsResponse) {
        collapsingToolBar.title = movie.original_title
        tvDescription.text = movie.overview
        tvTagLine.text = movie.tagline
        tvYearPublished.text = movie.release_date
        tvRating.text = "${movie.vote_average}/10"
        tvBudget.text = "$${movie.budget}"
    }

    private fun setupGenresRecyclerView() {
        genresAdapter = GenresAdapter()
        rvMovieGenres.adapter = genresAdapter
    }
}