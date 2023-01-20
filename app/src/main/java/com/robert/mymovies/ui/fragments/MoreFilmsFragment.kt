package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.mymovies.R
import com.robert.mymovies.utils.Resource
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.robert.mymovies.adapters.AllFilmsAdapter
import com.robert.mymovies.adapters.GenresAdapter
import com.robert.mymovies.databinding.FragmentMoreFilmsBinding
import com.robert.mymovies.ui.MoreFilmsFragmentViewModel
import com.robert.mymovies.utils.Constants.QUERY_PAGE_SIZE
import com.robert.mymovies.utils.FilmType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MoreFilmsFragment: Fragment(R.layout.fragment_more_films) {
    private val filmsViewModel: MoreFilmsFragmentViewModel by viewModels()
    val args: MoreFilmsFragmentArgs by navArgs()

    private lateinit var filmsAdapter: AllFilmsAdapter
    private lateinit var genresAdapter: GenresAdapter

    private var _binding: FragmentMoreFilmsBinding? = null
    private val binding get() = _binding!!

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

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
        binding.genreShimmer.startShimmer()
        genresAdapter = GenresAdapter()
        //binding.rvGenres.adapter = genresAdapter
        setUpRecyclerView(args.type)
        // Checks from where this fragment was called and calls the appropriate methods
        if (args.type == "Movie") {
            filmsViewModel.getGenres(FilmType.MOVIE)
            getMovies(args.category)
            observeLiveData(view)
        }else{
            filmsViewModel.getGenres(FilmType.TVSHOW)
            getSeries(args.category)
            observeLiveData(view)
        }

        filmsViewModel.genres.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Resource.Status.SUCCESS -> {
                    //binding.genreShimmer.stopShimmer()
                    //binding.genreShimmer.visibility = View.GONE
                    //binding.rvGenres.visibility = View.VISIBLE
                    response.data?.let {
                        genresAdapter.updateList(it.genres)
                    }
                }
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {}
            }
        }
    }



    private fun observeLiveData(view: View) {
        filmsViewModel.allFilms.observe(viewLifecycleOwner){response ->
            when(response.status){
                Resource.Status.SUCCESS ->{
                    hideProgressBar()
                    response.data?.let {
                        filmsAdapter.differ.submitList(it.results.toList())

                        //Checking if it is last page
                        val totalPages = it.total_pages
                        isLastPage = filmsViewModel.allFilmsPage == totalPages

                        if(isLastPage){
                            binding.rvMoreFilms.setPadding(0,0,0,0)
                        }
                    }
                }
                Resource.Status.LOADING -> { showProgressbar()}
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

    private fun setUpRecyclerView(type: String) {
        val layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvMoreFilms.layoutManager = layoutManager
        filmsAdapter = AllFilmsAdapter(getDeviceWidth())
        binding.rvMoreFilms.adapter = filmsAdapter
        binding.rvMoreFilms.addOnScrollListener(this@MoreFilmsFragment.scrollListener)

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

    private fun getDeviceWidth(): Int {
        val displayMetrics = resources.displayMetrics
        val paddingWidthInDp = 26 // width in dp
        val paddingWidthInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                paddingWidthInDp.toFloat(), resources.displayMetrics).toInt()
        return displayMetrics.widthPixels - paddingWidthInPx
    }

    private fun showProgressbar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar(){
        isLoading = false
        binding.paginationProgressBar.visibility = View.INVISIBLE
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
                filmsViewModel.getPopularFilms(FilmType.TVSHOW)
            }
            "onAir" -> {
                filmsViewModel.getOnAirFilms(FilmType.TVSHOW)
            }
        }
    }

    private fun getMovies(category: String) {
        when(category){
            "popular" -> {
                filmsViewModel.getPopularFilms(FilmType.MOVIE)
            }
            "upcoming" -> {
                filmsViewModel.getUpcomingFilms(FilmType.MOVIE)
            }
        }
    }

}


