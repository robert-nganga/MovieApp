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
import com.robert.mymovies.data.remote.Movie
import com.robert.mymovies.utils.Constants.MOVIE_POSTER_BASE_URL

class AllMoviesAdapter(private val deviceWidth: Int): RecyclerView.Adapter<AllMoviesAdapter.AllMoviesViewHolder>() {


    private val differCallBack = object: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
    private var onItemClickListener: ((Movie)->Unit)? = null

    fun setOnItemClickListener(listener: (Movie)-> Unit){
        onItemClickListener = listener
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMoviesViewHolder {
        return AllMoviesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false))
    }

    override fun onBindViewHolder(holder: AllMoviesViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.setData(movie)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class AllMoviesViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        private val imgMoviePoster = itemView.findViewById<ImageView>(R.id.imgMoviePoster)
        private val tvMovieTitle = itemView.findViewById<TextView>(R.id.tvMovieTitle)

        fun setData(movie: Movie){
            val imageUrl = "$MOVIE_POSTER_BASE_URL${movie.poster_path}"
            val layoutParams = imgMoviePoster.layoutParams
            layoutParams.width = deviceWidth/2
            imgMoviePoster.layoutParams = layoutParams

            Glide.with(itemView).load(imageUrl).into(imgMoviePoster)
            tvMovieTitle.text = movie.title
            setOnItemClickListener {
                onItemClickListener?.let{ it(movie)}
            }
        }
    }
}