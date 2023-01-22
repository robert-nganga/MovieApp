package com.robert.mymovies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robert.mymovies.R
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
        return FilmViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.trending_item, parent, false))
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = differ.currentList[position]
        holder.itemView.setOnClickListener {onItemClickListener?.let { it(film) }}
        holder.setData(film)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class FilmViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val imgTrending = itemView.findViewById<ImageView>(R.id.imgTrending)

        fun setData(film: Film){
            val imageUrl = "$MOVIE_POSTER_BASE_URL${film.posterPath}"
            Glide.with(itemView)
                .load(imageUrl)
                .error(R.drawable.error_movie)
                .into(imgTrending)
        }
    }
}