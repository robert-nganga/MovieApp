package com.robert.mymovies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robert.mymovies.R
import com.robert.mymovies.databinding.MovieItemBinding
import com.robert.mymovies.databinding.TrendingItemBinding
import com.robert.mymovies.model.Film
import com.robert.mymovies.utils.Constants

class AllFilmsAdapter(private val deviceWidth: Int): RecyclerView.Adapter<AllFilmsAdapter.AllFilmsViewHolder>() {


    private val differCallBack = object: DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem == newItem
        }
    }
    private var onItemClickListener: ((Film)->Unit)? = null

    fun setOnItemClickListener(listener: (Film)-> Unit){
        onItemClickListener = listener
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllFilmsViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllFilmsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllFilmsViewHolder, position: Int) {
        val film = differ.currentList[position]
        holder.itemView.setOnClickListener { onItemClickListener?.let { it(film) }}
        holder.setData(film)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class AllFilmsViewHolder(private val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root) {
        private var currentCast: Film? = null

        fun setData(film: Film){
            currentCast = film
            val imageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${film.posterPath}"
            val layoutParams = binding.imgMoviePoster.layoutParams
            layoutParams.width = deviceWidth/2
            binding.imgMoviePoster.layoutParams = layoutParams

            Glide.with(itemView).load(imageUrl).error(R.drawable.error_movie).into(binding.imgMoviePoster)
            binding.tvMovieTitle.text = film.title
        }
    }
}