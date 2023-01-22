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
import com.robert.mymovies.ui.FilmViewModel
import com.robert.mymovies.ui.MainActivity
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


        binding.popularShimmerSeries.startShimmer()
        binding.onAirShimmerSeries.startShimmer()
        binding.topRatedShimmerSeries.startShimmer()
        binding.sliderShimmerSeries.startShimmer()

        binding.rvOnAirSeries.adapter = onAirAdapter
        binding.rvPopularSeries.adapter = popularAdapter
        binding.rvTopRatedSeries.adapter = topRatedAdapter

        //Set Adapter listeners
        onAirAdapter.setOnItemClickListener {
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

        viewModel.allTrendingFilms.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Resource.Status.SUCCESS -> {
                    binding.sliderShimmerSeries.stopShimmer()
                    binding.sliderShimmerSeries.visibility = View.INVISIBLE
                    binding.seriesCardSlider.visibility = View.VISIBLE
                    response.data?.let {
                        it.results.forEach { series ->
                            val imageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${series.backdropPath}"
                            imageList.add(SlideModel(imageUrl, series.title))
                        }
                        binding.seriesImageSlider.setImageList(imageList, ScaleTypes.FIT)
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {error = response.message}
            }
        }

        viewModel.allTopRatedFilms.observe(viewLifecycleOwner){ response->
            when(response.status){
                Resource.Status.SUCCESS ->{
                    binding.topRatedShimmerSeries.stopShimmer()
                    binding.topRatedShimmerSeries.visibility = View.INVISIBLE
                    binding.rvTopRatedSeries.visibility = View.VISIBLE
                    response.data?.let {
                        topRatedAdapter.differ.submitList(it.results.toList())
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {error = response.message}
            }
        }
        viewModel.allPopularFilms.observe(viewLifecycleOwner){ response ->

            when(response.status){
                Resource.Status.SUCCESS ->{
                    binding.popularShimmerSeries.stopShimmer()
                    binding.popularShimmerSeries.visibility = View.INVISIBLE
                    binding.rvPopularSeries.visibility = View.VISIBLE
                    response.data?.let {
                        popularAdapter.differ.submitList(it.results.toList())
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {error = response.message}
            }

        }

        viewModel.allOnAirFilms.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Resource.Status.SUCCESS ->{
                    binding.onAirShimmerSeries.stopShimmer()
                    binding.onAirShimmerSeries.visibility = View.INVISIBLE
                    binding.rvOnAirSeries.visibility = View.VISIBLE
                    response.data?.let {
                        onAirAdapter.differ.submitList(it.results.toList())
                    }
                    if(error != null){
                        displayError(view, error)
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {
                    if(error != null){
                        displayError(view, error)
                    }else{
                        displayError(view, response.message)
                    }
                }
            }
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