package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.robert.mymovies.R
import com.robert.mymovies.adapters.FilmLoadStateAdapter
import com.robert.mymovies.adapters.SearchAdapter
import com.robert.mymovies.databinding.FragmentSearchBinding
import com.robert.mymovies.model.Search
import com.robert.mymovies.utils.Resource
import com.robert.mymovies.viewmodels.SearchViewModel
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
        binding.rvSearch.adapter = searchAdapter.withLoadStateFooter(
            footer = FilmLoadStateAdapter{ searchAdapter.retry() }
        )

        binding.retryButton.setOnClickListener { searchAdapter.retry() }
        searchAdapter.addLoadStateListener { loadState->
            val isListEmpty = loadState.refresh is LoadState.Error
            // show empty list
            binding.emptyList.isVisible = isListEmpty

            // Only show the list if refresh succeeds.
            binding.rvSearch.isVisible = !isListEmpty

            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading

            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.refresh is LoadState.Error
        }

        binding.imageButton.setOnClickListener {
            findNavController().popBackStack()
        }

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
                //Creates a simple delay after the user finishes typing
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    newText?.let{ query ->
                        if(query.isNotEmpty()){
                            viewModel.getQuery(query)
                        }
                    }
                }
                return true
            }
        })

        viewModel.searchFilms.observe(viewLifecycleOwner){ response ->
            searchAdapter.submitData(viewLifecycleOwner.lifecycle,response)

        }
    }

}