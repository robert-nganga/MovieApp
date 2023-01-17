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
import com.robert.mymovies.data.remote.Series
import com.robert.mymovies.utils.Constants

class AllSeriesAdapter(private val deviceWidth: Int): RecyclerView.Adapter<AllSeriesAdapter.AllMoviesViewHolder>() {


    private val differCallBack = object: DiffUtil.ItemCallback<Series>() {
        override fun areItemsTheSame(oldItem: Series, newItem: Series): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Series, newItem: Series): Boolean {
            return oldItem == newItem
        }
    }
    private var onItemClickListener: ((Series)->Unit)? = null

    fun setOnItemClickListener(listener: (Series)-> Unit){
        onItemClickListener = listener
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMoviesViewHolder {
        return AllMoviesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false))
    }

    override fun onBindViewHolder(holder: AllMoviesViewHolder, position: Int) {
        val series = differ.currentList[position]
        holder.itemView.setOnClickListener { onItemClickListener?.let { it(series) }}
        holder.setData(series)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class AllMoviesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val imgMoviePoster = itemView.findViewById<ImageView>(R.id.imgMoviePoster)
        private val tvMovieTitle = itemView.findViewById<TextView>(R.id.tvMovieTitle)
        private var currentCast: Series? = null

        fun setData(series: Series){
            currentCast = series
            val imageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${series.poster_path}"
            val layoutParams = imgMoviePoster.layoutParams
            layoutParams.width = deviceWidth/2
            imgMoviePoster.layoutParams = layoutParams

            Glide.with(itemView).load(imageUrl).into(imgMoviePoster)
            tvMovieTitle.text = series.name
        }
    }
}