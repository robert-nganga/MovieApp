package com.robert.mymovies.ui.fragments

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.robert.mymovies.R
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.robert.mymovies.adapters.AllFilmsAdapter
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


        getMoreFilms(if(args.type == "Movie") FilmType.MOVIE else FilmType.TVSHOW, args.category)


    }

    private fun getMoreFilms(filmType: FilmType, category: String) = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.getMoreFilms(filmType, category).collect{
                filmsAdapter.submitData(it)
            }
        }
    }

    private fun setUpRecyclerView() {
        Log.i("MoreFilmsFragment", getDeviceWidth().toString())
        val layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvMoreFilms.layoutManager = layoutManager
        filmsAdapter = AllFilmsAdapter(getDeviceWidth())
        binding.rvMoreFilms.adapter = filmsAdapter

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
        Log.i("MoreFilmsFragment", displayMetrics.widthPixels.toString())
        return displayMetrics.widthPixels - paddingWidthInPx
    }

    private fun displayError(view: View, message: String?) {
        if (message != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
                setAction("Retry"){
                }.show()
            }
        }
    }

}


