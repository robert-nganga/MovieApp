package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.robert.mymovies.R
import com.robert.mymovies.adapters.GenresAdapter
import com.robert.mymovies.adapters.MovieAdapter
import com.robert.mymovies.adapters.SeriesAdapter
import com.robert.mymovies.ui.MainActivity
import com.robert.mymovies.ui.MoviesFragmentViewModel
import com.robert.mymovies.ui.SeriesFragmentViewModel
import com.robert.mymovies.utils.Constants
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeriesFragment: Fragment(R.layout.fragment_series) {
    private val viewModel: SeriesFragmentViewModel by viewModels()
    private var error: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val popularAdapter = SeriesAdapter()
        //val trendingAdapter = SeriesAdapter()
        val upcomingAdapter = SeriesAdapter()


        val imageSlider = view.findViewById<ImageSlider>(R.id.seriesImageSlider)
        val imageList = ArrayList<SlideModel>()

        val tvMorePopular = view.findViewById<TextView>(R.id.tvMorePopularSeries)
        tvMorePopular.setOnClickListener {
            val bundle = Bundle().apply {
                putString("type", "Series")
                putString("category", "popular")
            }
            findNavController().navigate(R.id.action_seriesFragment_to_moreFilmsFragment, bundle)
        }
        val tvMoreUpcoming = view.findViewById<TextView>(R.id.tvMoreUpcoming)
        tvMoreUpcoming.setOnClickListener {
            val bundle = Bundle().apply {
                putString("type", "Series")
                putString("category", "onAir")
            }
            findNavController().navigate(R.id.action_seriesFragment_to_moreFilmsFragment, bundle)
        }

        //val shimmerTrending = view.findViewById<ShimmerFrameLayout>(R.id.trendingShimmer)
        //shimmerTrending.startShimmer()
        val shimmerPopular = view.findViewById<ShimmerFrameLayout>(R.id.popularShimmerSeries)
        shimmerPopular.startShimmer()
        val shimmerUpcoming = view.findViewById<ShimmerFrameLayout>(R.id.upcomingShimmerSeries)
        shimmerUpcoming.startShimmer()

        //Recyclerview Layouts
        val upcomingRecyclerView = view.findViewById<RecyclerView>(R.id.rvUpcomingSeries)
        //val trendingRecyclerView = view.findViewById<RecyclerView>(R.id.rvTrending)
        val popularRecyclerView = view.findViewById<RecyclerView>(R.id.rvPopularSeries)
        upcomingRecyclerView.adapter = upcomingAdapter
        //trendingRecyclerView.adapter = trendingAdapter
        popularRecyclerView.adapter = popularAdapter

        //Set Adapter listeners
        upcomingAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("seriesId", it.id)
            }
            findNavController().navigate(R.id.action_seriesFragment_to_seriesDetailsFragment, bundle)
        }

        popularAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("seriesId", it.id)
            }
            findNavController().navigate(R.id.action_seriesFragment_to_seriesDetailsFragment, bundle)
        }

        viewModel.allTopRatedSeries.observe(viewLifecycleOwner){ response->
            when(response.status){
                Resource.Status.SUCCESS ->{
//                    shimmerTrending.stopShimmer()
//                    shimmerTrending.visibility = View.INVISIBLE
//                    trendingRecyclerView.visibility = View.VISIBLE
                    response.data?.let {
                        //trendingAdapter.updateList(it.series)
                        it.results.forEach { series ->
                            val imageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${series.backdrop_path}"
                            imageList.add(SlideModel(imageUrl, series.name))
                        }
                        imageSlider.setImageList(imageList, ScaleTypes.FIT)
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {error = response.message}
            }
        }
        viewModel.allPopularSeries.observe(viewLifecycleOwner){ response ->

            when(response.status){
                Resource.Status.SUCCESS ->{
                    shimmerPopular.stopShimmer()
                    shimmerPopular.visibility = View.INVISIBLE
                    popularRecyclerView.visibility = View.VISIBLE
                    response.data?.let {
                        popularAdapter.differ.submitList(it.results.toList())
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {error = response.message}
            }

        }

        viewModel.allOnAirSeries.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Resource.Status.SUCCESS ->{
                    shimmerUpcoming.stopShimmer()
                    shimmerUpcoming.visibility = View.INVISIBLE
                    upcomingRecyclerView.visibility = View.VISIBLE
                    response.data?.let {
                        upcomingAdapter.differ.submitList(it.results.toList())
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {
                    if(error != null){
                        displayError(view, error)
                    }else{
                        displayError(view, response.message)
                    }
                }
            }

        }

    }

    private fun displayError(view: View, message: String?) {
        if (message != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
                setAction("Retry"){
                    viewModel.fetchData()
                }.show()
            }
        }
    }


}