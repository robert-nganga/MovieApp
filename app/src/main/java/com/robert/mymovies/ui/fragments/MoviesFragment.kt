package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.robert.mymovies.R
import com.robert.mymovies.adapters.AllMoviesAdapter
import com.robert.mymovies.adapters.GenresAdapter
import com.robert.mymovies.adapters.TrendingAdapter
import com.robert.mymovies.ui.MainActivity
import com.robert.mymovies.ui.MovieViewModel
import com.robert.mymovies.utils.Resource

class MoviesFragment: Fragment(R.layout.fragment_movies) {

    private lateinit var viewModel: MovieViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val popularAdapter = TrendingAdapter()
        val trendingAdapter = TrendingAdapter()
        val upcomingAdapter = TrendingAdapter()
        val genresAdapter = GenresAdapter()

        val tvMorePopular = view.findViewById<TextView>(R.id.tvMorePopular)
        tvMorePopular.setOnClickListener {
            val bundle = Bundle().apply {
                putString("category", "popular")
            }
            findNavController().navigate(R.id.action_moviesFragment_to_moreFilmsFragment, bundle)
        }
        val tvMoreUpcoming = view.findViewById<TextView>(R.id.tvMoreUpcoming)
        tvMoreUpcoming.setOnClickListener {
            val bundle = Bundle().apply {
                putString("category", "upcoming")
            }
            findNavController().navigate(R.id.action_moviesFragment_to_moreFilmsFragment, bundle)
        }

        // Shimmer Layouts
        val shimmerGenre = view.findViewById<ShimmerFrameLayout>(R.id.genreShimmer)
        shimmerGenre.startShimmer()
        val shimmerTrending = view.findViewById<ShimmerFrameLayout>(R.id.trendingShimmer)
        shimmerTrending.startShimmer()
        val shimmerPopular = view.findViewById<ShimmerFrameLayout>(R.id.popularShimmer)
        shimmerPopular.startShimmer()
        val shimmerUpcoming = view.findViewById<ShimmerFrameLayout>(R.id.upcomingShimmer)
        shimmerUpcoming.startShimmer()

        //Recyclerview Layouts
        val upcomingRecyclerView = view.findViewById<RecyclerView>(R.id.rvUpcoming)
        val trendingRecyclerView = view.findViewById<RecyclerView>(R.id.rvTrending)
        val popularRecyclerView = view.findViewById<RecyclerView>(R.id.rvPopular)
        val genresRecyclerView = view.findViewById<RecyclerView>(R.id.rvGenres)
        upcomingRecyclerView.adapter = upcomingAdapter
        genresRecyclerView.adapter = genresAdapter
        trendingRecyclerView.adapter = trendingAdapter
        popularRecyclerView.adapter = popularAdapter
        viewModel.allGenres.observe(viewLifecycleOwner){ response->
            when(response.status){
                Resource.Status.SUCCESS -> {
                    shimmerGenre.stopShimmer()
                    shimmerGenre.visibility = View.INVISIBLE
                    genresRecyclerView.visibility = View.VISIBLE
                    response.data?.let {
                        genresAdapter.updateList(it.genres)
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {}
            }
        }
        viewModel.allTrendingMovies.observe(viewLifecycleOwner){ response->
            when(response.status){
                Resource.Status.SUCCESS ->{
                    shimmerTrending.stopShimmer()
                    shimmerTrending.visibility = View.INVISIBLE
                    trendingRecyclerView.visibility = View.VISIBLE
                    response.data?.let {
                        trendingAdapter.updateList(it.results)
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {
                    response.message?.let{message ->
                        Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
                            setAction("Retry"){
                                viewModel.fetchData()
                            }
                        }
                    }
                }
            }
        }
        viewModel.allPopularMovies.observe(viewLifecycleOwner){ response ->

            when(response.status){
                Resource.Status.SUCCESS ->{
                    shimmerPopular.stopShimmer()
                    shimmerPopular.visibility = View.INVISIBLE
                    popularRecyclerView.visibility = View.VISIBLE
                    response.data?.let {
                        popularAdapter.updateList(it.results)
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {}
            }

        }

        viewModel.allUpcomingMovies.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Resource.Status.SUCCESS ->{
                    shimmerUpcoming.stopShimmer()
                    shimmerUpcoming.visibility = View.INVISIBLE
                    upcomingRecyclerView.visibility = View.VISIBLE
                    response.data?.let {
                        upcomingAdapter.updateList(it.results)
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {}
            }

        }

    }
}