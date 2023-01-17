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
import com.robert.mymovies.data.remote.Movie
import com.robert.mymovies.data.remote.Series
import com.robert.mymovies.utils.Constants

class SeriesAdapter: RecyclerView.Adapter<SeriesAdapter.TrendingViewHolder>() {

    private val differCallBack = object: DiffUtil.ItemCallback<Series>() {
        override fun areItemsTheSame(oldItem: Series, newItem: Series): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Series, newItem: Series): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    private var onItemClickListener: ((Series)->Unit)? = null

    fun setOnItemClickListener(listener: (Series)-> Unit){
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        return TrendingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.trending_item, parent, false))
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        val series = differ.currentList[position]
        holder.itemView.setOnClickListener {onItemClickListener?.let { it(series) }}
        holder.setData(series)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class TrendingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val imgTrending = itemView.findViewById<ImageView>(R.id.imgTrending)

        fun setData(movie: Series){
            val imageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${movie.poster_path}"
            Glide.with(itemView)
                .load(imageUrl)
                .into(imgTrending)
        }
    }
}