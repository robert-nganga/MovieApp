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
import com.google.android.material.snackbar.Snackbar
import com.robert.mymovies.R
import com.robert.mymovies.adapters.CastAdapter
import com.robert.mymovies.adapters.GenresAdapter
import com.robert.mymovies.adapters.MovieAdapter
import com.robert.mymovies.data.remote.MovieDetailsResponse
import com.robert.mymovies.ui.MovieFragmentViewModel
import com.robert.mymovies.utils.Constants
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import io.github.glailton.expandabletextview.ExpandableTextView

@AndroidEntryPoint
class MovieFragment: Fragment(R.layout.fragment_movie) {

    private val viewModel: MovieFragmentViewModel by viewModels()
    private lateinit var collapsingToolBar: CollapsingToolbarLayout
    private lateinit var imgToolBar: ImageView
    private lateinit var imgPoster: ImageView
    private lateinit var tvTagLine: TextView
    private lateinit var tvYearPublished: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvDuration: TextView
    private lateinit var tvDescription: ExpandableTextView
    private lateinit var rvMovieGenres: RecyclerView
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var rvCast: RecyclerView
    private lateinit var castAdapter: CastAdapter
    private lateinit var rvSimilar: RecyclerView
    private lateinit var similarAdapter: MovieAdapter
    private val args: MovieFragmentArgs by navArgs()

    private var errorMessage: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collapsingToolBar = view.findViewById(R.id.collapsingToolBar)
        imgToolBar = view.findViewById(R.id.imgToolBar)
        imgPoster = view.findViewById(R.id.imgPoster)
        tvDescription = view.findViewById(R.id.tvDescription)
        tvTagLine = view.findViewById(R.id.tvTagLine)
        tvYearPublished = view.findViewById(R.id.tvYearPublished)
        tvRating = view.findViewById(R.id.tvRating)
        tvDuration = view.findViewById(R.id.tvDuration)
        rvMovieGenres = view.findViewById(R.id.rvMovieGenres)
        rvCast= view.findViewById(R.id.rvCast)
        rvSimilar = view.findViewById(R.id.rvSimilar)
        setupGenresRecyclerView()
        setupCastRecyclerView()
        setupSimilarRecyclerView()
        //Fetching data using the id passed as argument
        if (viewModel.movieId != null) {
            viewModel.fetchData(viewModel.movieId!!)
        }else{
            viewModel.fetchData(args.id)
        }

        similarAdapter.setOnItemClickListener {
            // Fetching data using id of clicked similar movie
            viewModel.movieId = it.id
            viewModel.fetchData(it.id)
        }

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
                Resource.Status.ERROR -> {errorMessage = response.message}
            }
        }

        viewModel.castDetails.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        castAdapter.differ.submitList(it.cast)
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {errorMessage = response.message}
            }
        }

        viewModel.similarMovies.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        similarAdapter.differ.submitList(it.results.toList())
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {
                    if (errorMessage != null){
                        displayError(view, errorMessage)
                    }else{
                        displayError(view, response.message)
                    }
                }
            }
        }
    }

    private fun setupSimilarRecyclerView() {
        similarAdapter = MovieAdapter()
        rvSimilar.adapter = similarAdapter
    }

    private fun setupCastRecyclerView() {
        castAdapter = CastAdapter()
        rvCast.adapter = castAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun setMovieDetails(movie: MovieDetailsResponse) {
        collapsingToolBar.title = movie.title
        tvDescription.text = movie.overview
        tvTagLine.text = movie.tagline
        tvTagLine.maxLines = 5
        tvYearPublished.text = movie.release_date
        tvRating.text = "Rating ⭐: ${movie.vote_average}/10"
        tvDuration.text = "Duration: ${movie.runtime} Mins"
    }

    private fun setupGenresRecyclerView() {
        genresAdapter = GenresAdapter()
        rvMovieGenres.adapter = genresAdapter
    }

    private fun displayError(view: View, message: String?) {
        if (message != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
                setAction("Retry"){
                    if (viewModel.movieId != null) {
                        viewModel.fetchData(viewModel.movieId!!)
                    }else{
                        viewModel.fetchData(args.id)
                    }
                }.show()
            }
        }
    }
}