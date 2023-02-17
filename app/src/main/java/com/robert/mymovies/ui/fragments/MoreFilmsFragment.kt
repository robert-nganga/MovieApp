package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.robert.mymovies.R
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.robert.mymovies.adapters.AllFilmsAdapter
import com.robert.mymovies.adapters.AllFilmsAdapter.Companion.LOADING_ITEM
import com.robert.mymovies.adapters.FilmLoadStateAdapter
import com.robert.mymovies.adapters.GenresAdapter
import com.robert.mymovies.databinding.FragmentMoreFilmsBinding
import com.robert.mymovies.utils.FilmType
import com.robert.mymovies.viewmodels.MoreFilmsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MoreFilmsFragment: Fragment(R.layout.fragment_more_films) {
    private val viewModel: MoreFilmsFragmentViewModel by viewModels()
    private val args: MoreFilmsFragmentArgs by navArgs()

    private lateinit var filmsAdapter: AllFilmsAdapter
    private lateinit var genresAdapter: GenresAdapter

    private var _binding: FragmentMoreFilmsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreFilmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        genresAdapter = GenresAdapter()
        setUpRecyclerView()

        viewModel.getDetails(args.type, args.category)
        binding.retryButton.setOnClickListener { filmsAdapter.retry() }
        viewLifecycleOwner.lifecycleScope.launch {
            filmsAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty = loadState.refresh is LoadState.Error
                // show empty list
                binding.emptyList.isVisible = isListEmpty

                // Only show the list if refresh succeeds.
                binding.rvMoreFilms.isVisible = !isListEmpty

                // Show loading spinner during initial load or refresh.
                binding.progressBar.isVisible = loadState.refresh is LoadState.Loading

                // Show the retry state if initial load or refresh fails.
                binding.retryButton.isVisible = loadState.refresh is LoadState.Error

            }
        }

        viewModel.films.observe(viewLifecycleOwner) {
            filmsAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun setUpRecyclerView() {
        Log.i("MoreFilmsFragment", getDeviceWidth().toString())
        val span = if (getDeviceWidth() > 700) 3 else 2
        val gridLayoutManager = GridLayoutManager(requireContext(), span, GridLayoutManager.VERTICAL, false)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (filmsAdapter.getItemViewType(position) == LOADING_ITEM)
                    1 else span
            }
        }
        binding.rvMoreFilms.layoutManager = gridLayoutManager
        filmsAdapter = AllFilmsAdapter(getImageWidth())
        binding.rvMoreFilms.adapter = filmsAdapter.withLoadStateFooter(
            footer = FilmLoadStateAdapter { filmsAdapter.retry() }
        )

        filmsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it.id)
            }
            if (args.type == "Movie") {
                findNavController().navigate(
                    R.id.action_moreFilmsFragment_to_movieFragment,
                    bundle
                )
            }else{
                findNavController().navigate(R.id.action_moreFilmsFragment_to_seriesDetailsFragment, bundle)
            }
        }
    }

    private fun displayError(view: View, message: String?) {
        if (message != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
                setAction("Retry"){
                }.show()
            }
        }
    }

    private fun getImageWidth(): Int {
        val deviceWidth = getDeviceWidth()
        val paddingWidthInDp = 26 // padding in dp
        val paddingWidthInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            paddingWidthInDp.toFloat(), resources.displayMetrics).toInt()
        val width = deviceWidth - paddingWidthInPx
        return if (width > 700) {
            width / 3
        } else {
            width / 2
        }
    }

    private fun getDeviceWidth(): Int {
        val displayMetrics = resources.displayMetrics
        return displayMetrics.widthPixels
    }

}


