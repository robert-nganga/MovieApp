package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.mymovies.R
import com.robert.mymovies.adapters.AllMoviesAdapter
import com.robert.mymovies.ui.MainActivity
import com.robert.mymovies.ui.MovieViewModel
import com.robert.mymovies.utils.Resource
import android.util.DisplayMetrics

class MoreFilmsFragment: Fragment(R.layout.fragment_more_films) {

    private lateinit var viewModel: MovieViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        viewModel.getPopularMovies()

        val layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvMoreFilms)
        recyclerView.layoutManager = layoutManager
        val adapter = AllMoviesAdapter(getDeviceWidth())
        recyclerView.adapter = adapter
        viewModel.allPopularMovies.observe(viewLifecycleOwner){response ->
            when(response){
                is Resource.Success ->{
                    response.data?.let {
                        adapter.updateList(it.results)
                    }
                }
                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }
    }

    private fun getDeviceWidth(): Int {
        val displayMetrics = resources.displayMetrics
        val paddingWidthInDp = 26 // width in dp
        val paddingWidthInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingWidthInDp.toFloat(), resources.displayMetrics).toInt()
        return displayMetrics.widthPixels - paddingWidthInPx
    }

}


