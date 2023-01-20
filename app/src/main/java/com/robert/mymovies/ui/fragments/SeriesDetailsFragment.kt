package com.robert.mymovies.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.robert.mymovies.adapters.FilmAdapter
import com.robert.mymovies.adapters.GenresAdapter
import com.robert.mymovies.data.remote.SeriesDetailsResponse
import com.robert.mymovies.databinding.FragmentMovieBinding
import com.robert.mymovies.databinding.FragmentMoviesBinding
import com.robert.mymovies.databinding.FragmentSeriesDetailsBinding
import com.robert.mymovies.ui.MovieDetailsViewModel
import com.robert.mymovies.ui.SeriesDetailsViewModel
import com.robert.mymovies.utils.Constants
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import io.github.glailton.expandabletextview.ExpandableTextView

@AndroidEntryPoint
class SeriesDetailsFragment: Fragment(R.layout.fragment_series_details) {

    private val viewModel: SeriesDetailsViewModel by viewModels()
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var castAdapter: CastAdapter
    private lateinit var similarAdapter: FilmAdapter

    private val args: SeriesDetailsFragmentArgs by navArgs()

    private var _binding : FragmentSeriesDetailsBinding? = null
    private val binding get() = _binding!!

    private var errorMessage: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeriesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGenresRecyclerView()
        setupCastRecyclerView()
        setupSimilarRecyclerView()

        //Fetching data using the id passed as argument
        if (viewModel.seriesId != null) {
            viewModel.fetchData(viewModel.seriesId!!)
        }else{
            viewModel.fetchData(args.id)
        }

        similarAdapter.setOnItemClickListener {
            // Fetching data using id of clicked similar movie
            viewModel.seriesId = it.id
            viewModel.fetchData(it.id)
        }

        viewModel.seriesDetails.observe(viewLifecycleOwner){ response->
            when(response.status){
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        val backDropImageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${it.backdrop_path}"
                        val posterImageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${it.poster_path}"
                        Glide.with(view).load(backDropImageUrl).into(binding.imgToolBarSeries)
                        Glide.with(view).load(posterImageUrl).into(binding.imgPosterSeries)
                        displaySeriesDetails(it)
                        genresAdapter.updateList(it.genres)
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {errorMessage = response.message}
            }
        }

        viewModel.seriesCast.observe(viewLifecycleOwner){ response ->
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

        viewModel.similarSeries.observe(viewLifecycleOwner){ response ->
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

    @SuppressLint("SetTextI18n")
    private fun displaySeriesDetails(series: SeriesDetailsResponse){
        binding.collapsingToolBarSeries.title = series.name
        binding.tvDescriptionSeries.text = series.overview
        binding.tvTagLineSeries.text = series.tagline
        binding.tvTagLineSeries.text = series.first_air_date
        binding.tvRatingSeries.text = "Rating ⭐: ${series.vote_average}/10"
        binding.tvEpisodeDuration.text = "Episode runtime: ${series.episode_run_time} Mins"
    }

    private fun setupSimilarRecyclerView() {
        similarAdapter = FilmAdapter()
        binding.rvSimilarSeries.adapter = similarAdapter
    }

    private fun setupCastRecyclerView() {
        castAdapter = CastAdapter()
        binding.rvCastSeries.adapter = castAdapter
    }

    private fun setupGenresRecyclerView() {
        genresAdapter = GenresAdapter()
        binding.rvSeriesGenres.adapter = genresAdapter
    }

    private fun displayError(view: View, message: String?) {
        if (message != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
                setAction("Retry"){
                    if (viewModel.seriesId != null) {
                        viewModel.fetchData(viewModel.seriesId!!)
                    }else{
                        viewModel.fetchData(args.id)
                    }
                }.show()
            }
        }
    }
}