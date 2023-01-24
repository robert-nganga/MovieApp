package com.robert.mymovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robert.mymovies.R
import com.robert.mymovies.databinding.SearchItemBinding
import com.robert.mymovies.model.Search
import com.robert.mymovies.utils.Constants

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private val differCallBack = object: DiffUtil.ItemCallback<Search>() {
        override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
            return oldItem == newItem
        }
    }
    private var onItemClickListener: ((Search)->Unit)? = null

    fun setOnItemClickListener(listener: (Search)-> Unit){
        onItemClickListener = listener
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val search = differ.currentList[position]
        holder.itemView.setOnClickListener { onItemClickListener?.let { it(search) } }
        holder.setData(search)
    }

    override fun getItemCount(): Int = differ.currentList.size
    inner class SearchViewHolder(private val binding: SearchItemBinding):RecyclerView.ViewHolder(binding.root) {
        val adapter = GenresAdapter()
        fun setData(search: Search) {
            val imageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${search.posterPath}"
            Glide.with(itemView).load(imageUrl).error(R.drawable.error_movie).into(binding.imgSearch)
            binding.filmType.text = search.mediaType
            binding.searchTitle.text = search.title
            binding.searchDuration.text = search.releaseDate
            binding.searchRating.text = "Rating ⭐: ${search.voteAverage}"
            binding.rvSearchGenre.adapter = adapter
            search.genres?.let { adapter.updateList(it) }
        }
    }
}