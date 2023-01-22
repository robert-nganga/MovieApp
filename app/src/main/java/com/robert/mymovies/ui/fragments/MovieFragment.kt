package com.robert.mymovies.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.robert.mymovies.R
import com.robert.mymovies.adapters.CastAdapter
import com.robert.mymovies.adapters.GenresAdapter
import com.robert.mymovies.adapters.FilmAdapter
import com.robert.mymovies.data.remote.MovieDetailsResponse
import com.robert.mymovies.databinding.FragmentMovieBinding
import com.robert.mymovies.ui.MovieDetailsViewModel
import com.robert.mymovies.utils.Constants
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFragment: Fragment(R.layout.fragment_movie) {

    private val viewModel: MovieDetailsViewModel  by viewModels()
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var castAdapter: CastAdapter
    private lateinit var similarAdapter: FilmAdapter
    private val args: MovieFragmentArgs by navArgs()

    private var _binding : FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private var errorMessage: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            view.invalidate()
        }

        binding.movieGenresShimmer.startShimmer()
        binding.movieCastShimmer.startShimmer()
        binding.movieSimilarShimmer.startShimmer()

        viewModel.movieDetails.observe(viewLifecycleOwner){ response->
            when(response.status){
                Resource.Status.SUCCESS -> {

                    binding.movieGenresShimmer.stopShimmer()
                    binding.movieGenresShimmer.visibility = View.INVISIBLE
                    binding.rvMovieGenres.visibility = View.VISIBLE

                    response.data?.let {
                        val backDropImageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${it.backdrop_path}"
                        val posterImageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${it.poster_path}"
                        Glide.with(view).load(backDropImageUrl).into(binding.imgToolBar)
                        Glide.with(view).load(posterImageUrl).into(binding.imgPoster)
                        setMovieDetails(it)
                        genresAdapter.updateList(it.genres)
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {errorMessage = response.message}
            }
        }

        viewModel.movieCast.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Resource.Status.SUCCESS -> {
                    binding.movieCastShimmer.startShimmer()
                    binding.movieCastShimmer.visibility = View.INVISIBLE
                    binding.rvCast.visibility = View.VISIBLE
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
                    binding.movieSimilarShimmer.stopShimmer()
                    binding.movieSimilarShimmer.visibility = View.INVISIBLE
                    binding.rvSimilar.visibility = View.VISIBLE
                    response.data?.let {
                        similarAdapter.differ.submitList(it.results.toList())
                    }
                    if (errorMessage != null){
                        displayError(view, errorMessage)
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
        similarAdapter = FilmAdapter()
        binding.rvSimilar.adapter = similarAdapter
    }

    private fun setupCastRecyclerView() {
        castAdapter = CastAdapter()
        binding.rvCast.adapter = castAdapter
    }

    private fun setupGenresRecyclerView() {
        genresAdapter = GenresAdapter()
        binding.rvMovieGenres.adapter = genresAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun setMovieDetails(movie: MovieDetailsResponse) {
        binding.collapsingToolBar.title = movie.title
        binding.tvDescription.text = movie.overview
        binding.tvTagLine.text = movie.tagline
        binding.tvTagLine.maxLines = 4
        binding.tvYearPublished.text = movie.release_date
        binding.tvRating.text = "Rating ⭐: ${movie.vote_average}/10"
        binding.tvDuration.text = "Duration: ${movie.runtime} Mins"
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