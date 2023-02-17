package com.robert.mymovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robert.mymovies.R
import com.robert.mymovies.databinding.MovieItemBinding
import com.robert.mymovies.model.Film
import com.robert.mymovies.utils.Constants

class AllFilmsAdapter(private val imageWidth: Int): PagingDataAdapter<Film, AllFilmsAdapter.AllFilmsViewHolder>(differCallBack) {


    companion object{
        private val differCallBack = object: DiffUtil.ItemCallback<Film>() {
            override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
                return oldItem == newItem
            }
        }
        private const val SHOW_ITEM = 0
        const val LOADING_ITEM = 1
    }
    private var onItemClickListener: ((Film)->Unit)? = null

    fun setOnItemClickListener(listener: (Film)-> Unit){
        onItemClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllFilmsViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllFilmsViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) SHOW_ITEM else LOADING_ITEM
    }

    override fun onBindViewHolder(holder: AllFilmsViewHolder, position: Int) {
        val film = getItem(position)
        film?.let {
            holder.itemView.setOnClickListener { onItemClickListener?.let { it(film) }}
            holder.setData(film)
        }
    }


    inner class AllFilmsViewHolder(private val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root) {
        private var currentCast: Film? = null

        fun setData(film: Film){
            currentCast = film
            val imageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${film.posterPath}"
            val layoutParams = binding.imgMoviePoster.layoutParams
            layoutParams.width = imageWidth
            binding.imgMoviePoster.layoutParams = layoutParams

            Glide.with(itemView).load(imageUrl).error(R.drawable.error_movie).into(binding.imgMoviePoster)
            binding.tvMovieTitle.text = film.title
        }
    }
}