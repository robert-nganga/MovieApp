package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.mymovies.R
import com.robert.mymovies.adapters.AllMoviesAdapter
import com.robert.mymovies.utils.Resource
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.robert.mymovies.adapters.AllSeriesAdapter
import com.robert.mymovies.ui.MoreFilmsFragmentViewModel
import com.robert.mymovies.ui.MoreSeriesFragmentViewModel
import com.robert.mymovies.utils.Constants.QUERY_PAGE_SIZE
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MoreFilmsFragment: Fragment(R.layout.fragment_more_films) {
    private val moviesViewModel: MoreFilmsFragmentViewModel by viewModels()
    private val seriesViewModel: MoreSeriesFragmentViewModel by viewModels()
    val args: MoreFilmsFragmentArgs by navArgs()
    private lateinit var moviesAdapter: AllMoviesAdapter
    private lateinit var seriesAdapter: AllSeriesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var paginationProgressBar: ProgressBar

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paginationProgressBar = view.findViewById(R.id.paginationProgressBar)
        recyclerView = view.findViewById(R.id.rvMoreFilms)

        // Checks from where this fragment was called and calls the appropriate methods
        if (args.type == "Movie") {
            setUpMoviesRecyclerView()
            getMovies(args.category)
            observeMoviesLiveData(view)
        }else{
            setUpSeriesRecyclerView()
            getSeries(args.category)
            observeSeriesLiveData(view)
        }
    }

    private fun observeMoviesLiveData(view: View) {
        moviesViewModel.allFilms.observe(viewLifecycleOwner){response ->
            when(response.status){
                Resource.Status.SUCCESS ->{
                    hideProgressBar()
                    response.data?.let {
                        moviesAdapter.differ.submitList(it.results.toList())

                        //Checking if it is last page
                        val totalPages = it.total_pages
                        isLastPage = moviesViewModel.allFilmsPage == totalPages

                        if(isLastPage){
                            recyclerView.setPadding(0,0,0,0)
                        }
                    }
                }
                Resource.Status.LOADING -> {showProgressbar()}
                Resource.Status.ERROR -> {
                    hideProgressBar()
                    displayError(view, response.message)
                }
            }
        }
    }


    private fun observeSeriesLiveData(view: View) {
        seriesViewModel.allFilms.observe(viewLifecycleOwner){response ->
            when(response.status){
                Resource.Status.SUCCESS ->{
                    hideProgressBar()
                    response.data?.let {
                        seriesAdapter.differ.submitList(it.results.toList())

                        //Checking if it is last page
                        val totalPages = it.total_pages
                        isLastPage = seriesViewModel.allFilmsPage == totalPages

                        if(isLastPage){
                            recyclerView.setPadding(0,0,0,0)
                        }
                    }
                }
                Resource.Status.LOADING -> {showProgressbar()}
                Resource.Status.ERROR -> {
                    hideProgressBar()
                    displayError(view, response.message)
                }
            }
        }
    }

    //Pagination
    private val scrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                if(args.type == "Movie"){
                    getMovies(args.category)
                }else{
                    getSeries(args.category)
                }
                isScrolling = false
            }
        }
    }

    private fun setUpSeriesRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        seriesAdapter = AllSeriesAdapter(getDeviceWidth())
        recyclerView.adapter = seriesAdapter
        recyclerView.addOnScrollListener(this@MoreFilmsFragment.scrollListener)

        seriesAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putInt("id", it.id)
//            }
//            findNavController().navigate(R.id.action_moreFilmsFragment_to_movieFragment, bundle)
        }
    }

    private fun setUpMoviesRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        moviesAdapter = AllMoviesAdapter(getDeviceWidth())
        recyclerView.adapter = moviesAdapter
        recyclerView.addOnScrollListener(this@MoreFilmsFragment.scrollListener)

        moviesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it.id)
            }
            findNavController().navigate(R.id.action_moreFilmsFragment_to_movieFragment, bundle)
        }
    }

    private fun getDeviceWidth(): Int {
        val displayMetrics = resources.displayMetrics
        val paddingWidthInDp = 26 // width in dp
        val paddingWidthInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                paddingWidthInDp.toFloat(), resources.displayMetrics).toInt()
        return displayMetrics.widthPixels - paddingWidthInPx
    }

    private fun showProgressbar(){
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar(){
        isLoading = false
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun displayError(view: View, message: String?) {
        if (message != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
                setAction("Retry"){
                    if(args.type == "Movie"){
                        getMovies(args.category)
                    }else{
                        getSeries(args.category)
                    }
                }.show()
            }
        }
    }


    private fun getSeries(category: String) {
        when(category){
            "popular" -> {
                seriesViewModel.getPopularSeries()
            }
            "onAir" -> {
                seriesViewModel.getOnAirSeries()
            }
        }
    }

    private fun getMovies(category: String) {
        when(category){
            "popular" -> {
                moviesViewModel.getPopularMovies()
            }
            "upcoming" -> {
                moviesViewModel.getUpcomingMovies()
            }
        }
    }

}


