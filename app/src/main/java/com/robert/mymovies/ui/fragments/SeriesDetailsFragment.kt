package com.robert.mymovies.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.robert.mymovies.R
import com.robert.mymovies.adapters.CastAdapter
import com.robert.mymovies.adapters.GenresAdapter
import com.robert.mymovies.data.remote.SeriesDetailsResponse
import dagger.hilt.android.AndroidEntryPoint
import io.github.glailton.expandabletextview.ExpandableTextView

@AndroidEntryPoint
class SeriesDetailsFragment: Fragment(R.layout.fragment_series_details) {



    private lateinit var collapsingToolBar: CollapsingToolbarLayout
    private lateinit var imgToolBar: ImageView
    private lateinit var imgPoster: ImageView
    private lateinit var tvTagLine: TextView
    private lateinit var tvFirstAirDate: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvEpisodeDuration: TextView
    private lateinit var tvDescription: ExpandableTextView
    private lateinit var rvSeriesGenres: RecyclerView
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var rvCast: RecyclerView
    private lateinit var castAdapter: CastAdapter
    private lateinit var rvSimilar: RecyclerView
    //private lateinit var similarAdapter: SeriesAdapter
    private val args: SeriesDetailsFragmentArgs by navArgs()

    private var errorMessage: String? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collapsingToolBar = view.findViewById(R.id.collapsingToolBarSeries)
        imgToolBar = view.findViewById(R.id.imgToolBarSeries)
        imgPoster = view.findViewById(R.id.imgPosterSeries)
        tvDescription = view.findViewById(R.id.tvDescriptionSeries)
        tvTagLine = view.findViewById(R.id.tvTagLineSeries)
        tvFirstAirDate = view.findViewById(R.id.tvFirstAirDate)
        tvRating = view.findViewById(R.id.tvRatingSeries)
        tvEpisodeDuration = view.findViewById(R.id.tvEpisodeDuration)
        rvSeriesGenres = view.findViewById(R.id.rvSeriesGenres)
        rvCast= view.findViewById(R.id.rvCastSeries)
        rvSimilar = view.findViewById(R.id.rvSimilarSeries)

        setupGenresRecyclerView()
        setupCastRecyclerView()
        //setupSimilarRecyclerView()

//        //Fetching data using the id passed as argument
//        if (viewModel.seriesId != null) {
//            viewModel.fetchData(viewModel.seriesId!!)
//        }else{
//            viewModel.fetchData(args.seriesId)
//        }
//
//        similarAdapter.setOnItemClickListener {
//            // Fetching data using id of clicked similar movie
//            viewModel.seriesId = it.id
//            viewModel.fetchData(it.id)
//        }
//
//        viewModel.seriesDetails.observe(viewLifecycleOwner){ response->
//            when(response.status){
//                Resource.Status.SUCCESS -> {
//                    response.data?.let {
//                        val backDropImageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${it.backdrop_path}"
//                        val posterImageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${it.poster_path}"
//                        Glide.with(view).load(backDropImageUrl).into(imgToolBar)
//                        Glide.with(view).load(posterImageUrl).into(imgPoster)
//                        displaySeriesDetails(it)
//                        genresAdapter.updateList(it.genres)
//                    }
//                }
//                Resource.Status.LOADING -> {}
//                Resource.Status.ERROR -> {errorMessage = response.message}
//            }
//        }
//
//        viewModel.castDetails.observe(viewLifecycleOwner){ response ->
//            when(response.status){
//                Resource.Status.SUCCESS -> {
//                    response.data?.let {
//                        castAdapter.differ.submitList(it.cast)
//                    }
//                }
//                Resource.Status.LOADING -> {}
//                Resource.Status.ERROR -> {errorMessage = response.message}
//            }
//        }
//
//        viewModel.similarSeries.observe(viewLifecycleOwner){ response ->
//            when(response.status){
//                Resource.Status.SUCCESS -> {
//                    response.data?.let {
//                        similarAdapter.differ.submitList(it.results.toList())
//
//                        // Check if there was any error
//                        if (errorMessage != null){
//                            displayError(view, errorMessage)
//                        }else{
//                            displayError(view, response.message)
//                        }
//                    }
//                }
//                Resource.Status.LOADING -> {}
//                Resource.Status.ERROR -> {
//                    if (errorMessage != null){
//                        displayError(view, errorMessage)
//                    }else{
//                        displayError(view, response.message)
//                    }
//                }
//            }
//        }
    }

    @SuppressLint("SetTextI18n")
    private fun displaySeriesDetails(series: SeriesDetailsResponse){
        collapsingToolBar.title = series.name
        tvDescription.text = series.overview
        tvTagLine.text = series.tagline
        tvTagLine.maxLines = 5
        tvFirstAirDate.text = series.first_air_date
        tvRating.text = "Rating ⭐: ${series.vote_average}/10"
        tvEpisodeDuration.text = "Episode runtime: ${series.episode_run_time} Mins"
    }

//    private fun setupSimilarRecyclerView() {
//        similarAdapter = SeriesAdapter()
//        rvSimilar.adapter = similarAdapter
//    }

    private fun setupCastRecyclerView() {
        castAdapter = CastAdapter()
        rvCast.adapter = castAdapter
    }

    private fun setupGenresRecyclerView() {
        genresAdapter = GenresAdapter()
        rvSeriesGenres.adapter = genresAdapter
    }

    private fun displayError(view: View, message: String?) {
        if (message != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
                setAction("Retry"){
//                    if (viewModel.seriesId != null) {
//                        viewModel.fetchData(viewModel.seriesId!!)
//                    }else{
//                        viewModel.fetchData(args.seriesId)
//                    }
                }.show()
            }
        }
    }
}