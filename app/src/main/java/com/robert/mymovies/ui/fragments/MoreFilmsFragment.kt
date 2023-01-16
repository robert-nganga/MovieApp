package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
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
import com.robert.mymovies.ui.MoreFilmsFragmentViewModel
import com.robert.mymovies.utils.Constants.QUERY_PAGE_SIZE
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MoreFilmsFragment: Fragment(R.layout.fragment_more_films) {
    private val viewModel: MoreFilmsFragmentViewModel by viewModels()
    val args: MoreFilmsFragmentArgs by navArgs()
    private lateinit var adapter: AllMoviesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var paginationProgressBar: ProgressBar

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Checks which button was clicked and calls the appropriate method
        getAppropriateData(args.category)
        paginationProgressBar = view.findViewById(R.id.paginationProgressBar)
        recyclerView = view.findViewById(R.id.rvMoreFilms)
        setUpRecyclerView()

        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it.id)
            }
            findNavController().navigate(R.id.action_moreFilmsFragment_to_movieFragment, bundle)
        }

        viewModel.allFilms.observe(viewLifecycleOwner){response ->
            when(response.status){
                Resource.Status.SUCCESS ->{
                    hideProgressBar()
                    response.data?.let {
                        adapter.differ.submitList(it.results.toList())

                        //Checking if it is last page
                        val totalPages = it.total_pages
                        isLastPage = viewModel.allFilmsPage == totalPages

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
                getAppropriateData(args.category)
                isScrolling = false
            }
        }
    }

    private fun setUpRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        adapter = AllMoviesAdapter(getDeviceWidth())
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(this@MoreFilmsFragment.scrollListener)
    }

    private fun getAppropriateData(category: String) {
        when(category){
            "popular" -> {
                viewModel.getPopularMovies()
            }
            "upcoming" -> {
                viewModel.getUpcomingMovies()
            }
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
                    getAppropriateData(args.category)
                }.show()
            }
        }
    }

}


