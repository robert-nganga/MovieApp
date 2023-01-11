package com.robert.mymovies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robert.mymovies.R
import com.robert.mymovies.data.remote.Movie
import com.robert.mymovies.utils.Constants.MOVIE_POSTER_BASE_URL

class TrendingAdapter: RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder>() {

    private val movies = ArrayList<Movie>()

    fun updateList(newList: List<Movie>){
        movies.clear()
        movies.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        return TrendingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.trending_item, parent, false))
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        val movie = movies[position]
        holder.setData(movie)
    }

    override fun getItemCount(): Int = movies.size

    inner class TrendingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val imgTrending = itemView.findViewById<ImageView>(R.id.imgTrending)

        fun setData(movie: Movie){
            val imageUrl = "$MOVIE_POSTER_BASE_URL${movie.poster_path}"
            Glide.with(itemView)
                .load(imageUrl)
                .into(imgTrending)
        }
    }
}