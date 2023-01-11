package com.robert.mymovies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robert.mymovies.R
import com.robert.mymovies.data.remote.Movie
import com.robert.mymovies.utils.Constants.MOVIE_POSTER_BASE_URL

class AllMoviesAdapter: RecyclerView.Adapter<AllMoviesAdapter.AllMoviesViewHolder>() {

    private val movies = ArrayList<Movie>()

    fun updateList(newList: List<Movie>){
        if(movies.isNotEmpty()){
            movies.clear()
        }
        movies.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMoviesViewHolder {
        return AllMoviesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.img_movie_item, parent, false))
    }

    override fun onBindViewHolder(holder: AllMoviesViewHolder, position: Int) {
        val movie = movies[position]
        holder.setData(movie, position)
    }

    override fun getItemCount(): Int = movies.size

    inner class AllMoviesViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        private val imgMoviePoster = itemView.findViewById<ImageView>(R.id.imgMovie)

        fun setData(movie: Movie, position: Int){
            val imageUrl = "$MOVIE_POSTER_BASE_URL${movie.poster_path}"
            Glide.with(itemView)
                .load(imageUrl)
                .into(imgMoviePoster)
        }
    }
}