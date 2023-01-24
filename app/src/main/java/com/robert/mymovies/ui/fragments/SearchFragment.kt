package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.robert.mymovies.R
import com.robert.mymovies.adapters.SearchAdapter
import com.robert.mymovies.databinding.FragmentSearchBinding
import com.robert.mymovies.ui.MovieDetailsViewModel
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

        var job: Job? = null
        binding.tvSearch.addTextChangedListener { editable ->
            //Creates a simple delay so that the user can finish typing
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let{
                    if(editable.toString().isNotEmpty()){
                        viewModel.getSearchResults(editable.toString())
                    }
                }
            }
        }

        viewModel.searchResult.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Resource.Status.SUCCESS -> {
                    hideProgressBar()
                    response.data?.let {
                        searchAdapter.differ.submitList(it.results.toList())
                    }
                }
                Resource.Status.LOADING -> {showProgressbar()}
                Resource.Status.ERROR -> { hideProgressBar() }
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