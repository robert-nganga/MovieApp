package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.robert.mymovies.R
import com.robert.mymovies.adapters.AllMoviesAdapter
import com.robert.mymovies.adapters.GenresAdapter
import com.robert.mymovies.adapters.TrendingAdapter
import com.robert.mymovies.ui.MainActivity
import com.robert.mymovies.ui.MovieViewModel

class MoviesFragment: Fragment(R.layout.fragment_movies) {

    lateinit var viewModel: MovieViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val allMoviesAdapter = AllMoviesAdapter()
        val trendingAdapter = TrendingAdapter()
        val genresAdapter = GenresAdapter()

        // Shimmer Layouts
        val shimmerGenre = view.findViewById<ShimmerFrameLayout>(R.id.genreShimmer)
        shimmerGenre.startShimmer()
        val shimmerTrending = view.findViewById<ShimmerFrameLayout>(R.id.trendingShimmer)
        shimmerTrending.startShimmer()
        val shimmerPopular = view.findViewById<ShimmerFrameLayout>(R.id.popularShimmer)
        shimmerPopular.startShimmer()

        //Recyclerview Layouts
        val trendingRecyclerView = view.findViewById<RecyclerView>(R.id.rvTrending)
        val popularRecyclerView = view.findViewById<RecyclerView>(R.id.rvPopular)
        val genresRecyclerView = view.findViewById<RecyclerView>(R.id.rvGenres)
        genresRecyclerView.adapter = genresAdapter
        trendingRecyclerView.adapter = trendingAdapter
        popularRecyclerView.adapter = allMoviesAdapter
        viewModel.allGenres.observe(viewLifecycleOwner){
            shimmerGenre.stopShimmer()
            shimmerGenre.visibility = View.INVISIBLE
            genresRecyclerView.visibility = View.VISIBLE
            genresAdapter.updateList(it)
        }
        viewModel.allTrendingMovies.observe(viewLifecycleOwner){
            shimmerTrending.stopShimmer()
            shimmerTrending.visibility = View.INVISIBLE
            trendingRecyclerView.visibility = View.VISIBLE
            trendingAdapter.updateList(it)
        }
        viewModel.allPopularMovies.observe(viewLifecycleOwner){
            shimmerPopular.stopShimmer()
            shimmerPopular.visibility = View.INVISIBLE
            popularRecyclerView.visibility = View.VISIBLE
            allMoviesAdapter.updateList(it)
        }

    }
}