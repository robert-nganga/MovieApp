package com.robert.mymovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robert.mymovies.R
import com.robert.mymovies.databinding.TrendingItemBinding
import com.robert.mymovies.model.Film
import com.robert.mymovies.utils.Constants.MOVIE_POSTER_BASE_URL

class FilmAdapter: RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    private val differCallBack = object: DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    private var onItemClickListener: ((Film)->Unit)? = null

    fun setOnItemClickListener(listener: (Film)-> Unit){
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding = TrendingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = differ.currentList[position]
        holder.itemView.setOnClickListener {onItemClickListener?.let { it(film) }}
        holder.setData(film)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class FilmViewHolder(private val binding: TrendingItemBinding): RecyclerView.ViewHolder(binding.root){

        fun setData(film: Film){
            val imageUrl = "$MOVIE_POSTER_BASE_URL${film.posterPath}"
            Glide.with(itemView)
                .load(imageUrl)
                .error(R.drawable.error_movie)
                .into(binding.imgTrending)
        }
    }
}