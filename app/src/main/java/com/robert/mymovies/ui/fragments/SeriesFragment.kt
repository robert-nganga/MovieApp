package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.snackbar.Snackbar
import com.robert.mymovies.R
import com.robert.mymovies.adapters.FilmAdapter
import com.robert.mymovies.databinding.FragmentSeriesBinding
import com.robert.mymovies.viewmodels.FilmViewModel
import com.robert.mymovies.utils.Constants
import com.robert.mymovies.utils.FilmType
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeriesFragment: Fragment(R.layout.fragment_series) {

    private var _binding: FragmentSeriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FilmViewModel by viewModels()
    private var error: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Fetch the data
        viewModel.fetchSeriesData(FilmType.TVSHOW)

        val popularAdapter = FilmAdapter()
        val onAirAdapter = FilmAdapter()
        val topRatedAdapter = FilmAdapter()

        val imageList = ArrayList<SlideModel>()

        // Set listeners for the see more buttons
        seeMoreButtonsClickListeners()

        binding.rvOnAirSeries.adapter = onAirAdapter
        binding.rvPopularSeries.adapter = popularAdapter
        binding.rvTopRatedSeries.adapter = topRatedAdapter

        //Set Adapter listeners
        filmAdapterClickListeners(popularAdapter, onAirAdapter, topRatedAdapter)

        viewModel.allTrendingFilms.observe(viewLifecycleOwner){ response ->
            if(response.status == Resource.Status.LOADING && response.data.isNullOrEmpty()){
                binding.sliderShimmerSeries.startShimmer()
            }
            response.data?.let {
                // Stop the shimmer effect regardless of the status
                if(it.isNotEmpty()){
                    binding.sliderShimmerSeries.stopShimmer()
                    binding.sliderShimmerSeries.visibility = View.INVISIBLE
                    binding.seriesCardSlider.visibility = View.VISIBLE
                }

                it.forEach { series ->
                    val imageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${series.backdropPath}"
                    imageList.add(SlideModel(imageUrl, series.title))
                }
                binding.seriesImageSlider.setImageList(imageList, ScaleTypes.FIT)
            }
            response.message?.let { error = it }
        }

        viewModel.allTopRatedFilms.observe(viewLifecycleOwner){ response->
            if(response.status == Resource.Status.LOADING && response.data.isNullOrEmpty()){
                binding.topRatedShimmerSeries.startShimmer()
            }
            response.data?.let {
                if (it.isNotEmpty()){
                    binding.topRatedShimmerSeries.stopShimmer()
                    binding.topRatedShimmerSeries.visibility = View.INVISIBLE
                    binding.rvTopRatedSeries.visibility = View.VISIBLE
                }

                topRatedAdapter.differ.submitList(it.toList())
            }
            response.message?.let { error = it }
        }

        viewModel.allPopularFilms.observe(viewLifecycleOwner){ response ->
            if(response.status == Resource.Status.LOADING && response.data.isNullOrEmpty()){
                binding.popularShimmerSeries.startShimmer()
            }
            response.data?.let {
                if (it.isNotEmpty()){
                    binding.popularShimmerSeries.stopShimmer()
                    binding.popularShimmerSeries.visibility = View.INVISIBLE
                    binding.rvPopularSeries.visibility = View.VISIBLE
                }
                popularAdapter.differ.submitList(it.toList())
            }
            response.message?.let { error = it }
        }

        viewModel.allOnAirFilms.observe(viewLifecycleOwner){ response ->
            if(response.status == Resource.Status.LOADING && response.data.isNullOrEmpty()){
                binding.onAirShimmerSeries.startShimmer()
            }

            response.data?.let {
                if (it.isNotEmpty()){
                    binding.onAirShimmerSeries.stopShimmer()
                    binding.onAirShimmerSeries.visibility = View.INVISIBLE
                    binding.rvOnAirSeries.visibility = View.VISIBLE
                }
                onAirAdapter.differ.submitList(it.toList())
            }
            if(error != null){
                displayError(view, error)
            }else{
                displayError(view, response.message)
            }
        }
    }

    private fun seeMoreButtonsClickListeners() {
        binding.tvMorePopularSeries.setOnClickListener {
            val bundle = Bundle().apply {
                putString("type", "Series")
                putString("category", "popular")
            }
            findNavController().navigate(R.id.action_seriesFragment_to_moreFilmsFragment, bundle)
        }

        binding.tvMoreOnAir.setOnClickListener {
            val bundle = Bundle().apply {
                putString("type", "Series")
                putString("category", "onAir")
            }
            findNavController().navigate(R.id.action_seriesFragment_to_moreFilmsFragment, bundle)
        }

        binding.tvMoreTopRatedSeries.setOnClickListener {
            val bundle = Bundle().apply {
                putString("type", "Series")
                putString("category", "topRated")
            }
            findNavController().navigate(R.id.action_seriesFragment_to_moreFilmsFragment, bundle)
        }
    }

    private fun filmAdapterClickListeners(
        popularAdapter: FilmAdapter,
        onAirAdapter: FilmAdapter,
        topRatedAdapter: FilmAdapter
    ) {

        onAirAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it.id)
            }
            findNavController().navigate(R.id.action_seriesFragment_to_seriesDetailsFragment, bundle)
        }

        topRatedAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it.id)
            }
            findNavController().navigate(R.id.action_seriesFragment_to_seriesDetailsFragment, bundle)
        }

        popularAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it.id)
            }
            findNavController().navigate(R.id.action_seriesFragment_to_seriesDetailsFragment, bundle)
        }
    }

    private fun displayError(view: View, message: String?) {
        if (message != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
                setAction("Retry"){
                    viewModel.fetchSeriesData(FilmType.TVSHOW)
                }.show()
            }
        }
    }


}