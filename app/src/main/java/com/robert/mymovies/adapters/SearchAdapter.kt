package com.robert.mymovies.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robert.mymovies.R
import com.robert.mymovies.databinding.SearchItemBinding
import com.robert.mymovies.model.Search
import com.robert.mymovies.utils.Constants

class SearchAdapter: PagingDataAdapter<Search, SearchAdapter.SearchViewHolder>(differCallBack) {


    private var onItemClickListener: ((Search)->Unit)? = null

    fun setOnItemClickListener(listener: (Search)-> Unit){
        onItemClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val search = getItem(position)
        search?.let {
            holder.itemView.setOnClickListener { onItemClickListener?.let { it(search) }}
            holder.setData(search)
        }
    }


    inner class SearchViewHolder(private val binding: SearchItemBinding):RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(search: Search) {
            val imageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${search.posterPath}"
            Glide.with(itemView).load(imageUrl).error(R.drawable.error_movie).into(binding.imgSearch)
            if(search.mediaType == "tv"){
                binding.filmType.text = "Series"
            }else{
                binding.filmType.text = search.mediaType
            }
            binding.searchTitle.text = search.title
            binding.searchReleaseDate.text = search.releaseDate
            binding.searchRating.text = "Rating ⭐: ${search.voteAverage}"
        }
    }

    companion object{
        private val differCallBack = object: DiffUtil.ItemCallback<Search>() {
            override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
                return oldItem == newItem
            }
        }
    }
}