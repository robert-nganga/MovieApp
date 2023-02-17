package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.snackbar.Snackbar
import com.robert.mymovies.R
import com.robert.mymovies.adapters.FilmAdapter
import com.robert.mymovies.adapters.ViewPagerAdapter
import com.robert.mymovies.databinding.FragmentMoviesBinding
import com.robert.mymovies.viewmodels.FilmViewModel
import com.robert.mymovies.utils.Constants
import com.robert.mymovies.utils.FilmType
import com.robert.mymovies.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.abs

@AndroidEntryPoint
class MoviesFragment: Fragment(R.layout.fragment_movies) {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FilmViewModel by viewModels()
    private var error: String? = null

    private lateinit var handler: Handler
    private val runnable by lazy {  Runnable {
        if (binding.viewPager2.currentItem == binding.viewPager2.adapter?.itemCount?.minus(1)) {
            binding.viewPager2.currentItem = 0
        } else{
        binding.viewPager2.currentItem = binding.viewPager2.currentItem + 1
        }
    }
    }

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
        // Fetch the data
        viewModel.fetchMovieData(FilmType.MOVIE)

        val popularAdapter = FilmAdapter()
        val upcomingAdapter = FilmAdapter()
        val topRatedAdapter = FilmAdapter()
        val viewPagerAdapter = ViewPagerAdapter()

        handler = Handler(Looper.myLooper()!!)

        // Set listeners for the see more buttons
        seeMoreButtonListeners()

        //set the recycler view adapters
        binding.rvUpcoming.adapter = upcomingAdapter
        binding.rvPopular.adapter = popularAdapter
        binding.rvTopRated.adapter = topRatedAdapter
        setupViewPager(viewPagerAdapter)
        viewPagerOnPageChangedCallBack()

        //Set listeners for each adapter
        filmAdapterClickListeners(popularAdapter, upcomingAdapter, topRatedAdapter)

        viewModel.allTrendingFilms.observe(viewLifecycleOwner){ response->
            response.data?.let {
                viewPagerAdapter.differ.submitList(it.toList())
            }
            response.message?.let { error = it }
        }

        viewModel.allPopularFilms.observe(viewLifecycleOwner){ response ->
            if (response.status == Resource.Status.LOADING && response.data.isNullOrEmpty()) {
                binding.popularShimmer.startShimmer()
            }
            response.data?.let {
                //Stop the shimmer effect is there is data regardless of the status
                if(it.isNotEmpty()){
                    binding.popularShimmer.stopShimmer()
                    binding.popularShimmer.visibility = View.INVISIBLE
                    binding.rvPopular.visibility = View.VISIBLE
                }
                popularAdapter.differ.submitList(it.toList())
            }
            response.message?.let { error = it }
        }

        viewModel.allUpcomingFilms.observe(viewLifecycleOwner){ response ->
            if (response.status == Resource.Status.LOADING && response.data.isNullOrEmpty()) {
                binding.upcomingShimmer.startShimmer()
            }
            response.data?.let {
                if(it.isNotEmpty()){
                    binding.upcomingShimmer.stopShimmer()
                    binding.upcomingShimmer.visibility = View.INVISIBLE
                    binding.rvUpcoming.visibility = View.VISIBLE
                }
                upcomingAdapter.differ.submitList(it.toList())
            }
            response.message?.let { error = it }
        }

        viewModel.allTopRatedFilms.observe(viewLifecycleOwner){ response ->
            if (response.status == Resource.Status.LOADING && response.data.isNullOrEmpty()) {
                binding.topRatedShimmer.startShimmer()
            }
            response.data?.let {
                if(it.isNotEmpty()){
                    binding.topRatedShimmer.stopShimmer()
                    binding.topRatedShimmer.visibility = View.INVISIBLE
                    binding.rvTopRated.visibility = View.VISIBLE
                }
                topRatedAdapter.differ.submitList(it.toList())
            }
            if(error != null){
                displayError(view, error)
            }else{
                displayError(view, response.message)
            }
        }
    }

    private fun viewPagerOnPageChangedCallBack() {
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 3000L)
            }
        })
    }

    private fun setupViewPager(viewPagerAdapter: ViewPagerAdapter) {
        binding.viewPager2.apply {
            adapter = viewPagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
            setPageTransformer { page, position ->
                val scaleFactor = 1 - abs(position) * 0.2f
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor
                page.alpha = 0.5f + (scaleFactor - 0.8f) / 0.2f * 0.5f
            }
        }

    }

    private fun seeMoreButtonListeners() {
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

        binding.tvMoreTopRated.setOnClickListener {
            val bundle = Bundle().apply {
                putString("type", "Movie")
                putString("category", "topRated")
            }
            findNavController().navigate(R.id.action_moviesFragment_to_moreFilmsFragment, bundle)
        }
    }

    private fun filmAdapterClickListeners(
        popularAdapter: FilmAdapter,
        upcomingAdapter: FilmAdapter,
        topRatedAdapter: FilmAdapter
    ) {
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

        topRatedAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it.id)
            }
            findNavController().navigate(R.id.action_moviesFragment_to_movieFragment, bundle)
        }
    }

    private fun displayError(view: View, message: String?) {
        Log.e("MoviesFragment", "Error: $message")
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

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 3000L)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}