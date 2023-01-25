package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.mymovies.R
import com.robert.mymovies.adapters.SearchAdapter
import com.robert.mymovies.databinding.FragmentSearchBinding
import com.robert.mymovies.model.Search
import com.robert.mymovies.ui.MovieDetailsViewModel
import com.robert.mymovies.utils.Constants
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = SearchAdapter()
        binding.rvSearch.adapter = searchAdapter
        binding.rvSearch.addOnScrollListener(searchScrollListener)

        searchAdapter.setOnItemClickListener { search ->
            val bundle = Bundle().apply {
                putInt("id", search.id!!)
            }
            if (search.mediaType == "movie"){
                findNavController().navigate(R.id.action_searchFragment_to_movieFragment, bundle)
            }else{
                findNavController().navigate(R.id.action_searchFragment_to_seriesDetailsFragment, bundle)
            }
        }

        var job: Job? = null
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Creates a simple delay so that the user can finish typing
                job?.cancel()
                job = MainScope().launch {
                    delay(200L)
                    newText?.let{ query ->
                        if(query.isNotEmpty()){
                            viewModel.getSearchResults(query)
                        }
                    }
                }
                return true
            }
        })

        viewModel.searchResult.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Resource.Status.SUCCESS -> {
                    hideProgressBar()
                    response.data?.let {

                        searchAdapter.differ.submitList(filteredList(it.results))
                    }
                }
                Resource.Status.LOADING -> {showProgressbar()}
                Resource.Status.ERROR -> { hideProgressBar() }
            }

        }
    }

    private fun filteredList(results: MutableList<Search>): List<Search> {
        return results.filter { search-> search.mediaType != "person" }
    }

    //Pagination
    private val searchScrollListener = object: RecyclerView.OnScrollListener(){
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
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                val text = binding.searchView.query
                if (text.isNotEmpty()) {
                    viewModel.getPagedSearchResults(text.toString())
                }
                isScrolling = false
            }
        }
    }

    private fun showProgressbar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
    }
}