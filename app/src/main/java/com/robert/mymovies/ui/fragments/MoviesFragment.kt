package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.snackbar.Snackbar
import com.robert.mymovies.R
import com.robert.mymovies.adapters.FilmAdapter
import com.robert.mymovies.databinding.FragmentMoviesBinding
import com.robert.mymovies.ui.FilmViewModel
import com.robert.mymovies.ui.MainActivity
import com.robert.mymovies.utils.Constants
import com.robert.mymovies.utils.FilmType
import com.robert.mymovies.utils.Resource

class MoviesFragment: Fragment(R.layout.fragment_movies) {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FilmViewModel
    private var error: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        // Fetch the data
        viewModel.fetchMovieData(FilmType.MOVIE)

        val popularAdapter = FilmAdapter()
        val upcomingAdapter = FilmAdapter()

        val imageList = ArrayList<SlideModel>()

        // Set listeners for the see more buttons
        binding.tvMorePopular.setOnClickListener {
            val bundle = Bundle().apply {
                putString("type", "Movie")
                putString("category", "popular")
            }
            findNavController().navigate(R.id.action_moviesFragment_to_moreFilmsFragment, bundle)
        }

        binding.tvMoreUpcoming.setOnClickListener {
            val bundle = Bundle().apply {
                putString("type", "Movie")
                putString("category", "upcoming")
            }
            findNavController().navigate(R.id.action_moviesFragment_to_moreFilmsFragment, bundle)
        }

        //Start the shimmer effect
        binding.popularShimmer.startShimmer()
        binding.upcomingShimmer.startShimmer()

        //set the recycler view adapters
        binding.rvUpcoming.adapter = upcomingAdapter
        binding.rvPopular.adapter = popularAdapter

        //Set listeners for each movie
        upcomingAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it.id)
            }
            findNavController().navigate(R.id.action_moviesFragment_to_movieFragment, bundle)
        }

        popularAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it.id)
            }
            findNavController().navigate(R.id.action_moviesFragment_to_movieFragment, bundle)
        }

        viewModel.allTrendingFilms.observe(viewLifecycleOwner){ response->
            when(response.status){
                Resource.Status.SUCCESS ->{
                    response.data?.let {
                        it.results.forEach { movie ->
                            val imageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${movie.backdropPath}"
                            imageList.add(SlideModel(imageUrl, movie.title ))
                        }
                        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {error = response.message}
            }
        }
        viewModel.allPopularFilms.observe(viewLifecycleOwner){ response ->

            when(response.status){
                Resource.Status.SUCCESS ->{
                    binding.popularShimmer.stopShimmer()
                    binding.popularShimmer.visibility = View.INVISIBLE
                    binding.rvPopular.visibility = View.VISIBLE
                    response.data?.let {
                        popularAdapter.differ.submitList(it.results.toList())
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {error = response.message}
            }

        }

        viewModel.allUpcomingFilms.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Resource.Status.SUCCESS ->{
                    binding.upcomingShimmer.stopShimmer()
                    binding.upcomingShimmer.visibility = View.INVISIBLE
                    binding.rvUpcoming.visibility = View.VISIBLE
                    response.data?.let {
                        upcomingAdapter.differ.submitList(it.results.toList())
                    }

                    //Check if there was an error
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
                    viewModel.fetchMovieData(FilmType.MOVIE)
                }.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}