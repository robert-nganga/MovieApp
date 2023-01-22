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
        return AllFilmsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false))
    }

    override fun onBindViewHolder(holder: AllFilmsViewHolder, position: Int) {
        val film = differ.currentList[position]
        holder.itemView.setOnClickListener { onItemClickListener?.let { it(film) }}
        holder.setData(film)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class AllFilmsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val imgMoviePoster = itemView.findViewById<ImageView>(R.id.imgMoviePoster)
        private val tvMovieTitle = itemView.findViewById<TextView>(R.id.tvMovieTitle)
        private var currentCast: Film? = null

        fun setData(film: Film){
            currentCast = film
            val imageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${film.posterPath}"
            val layoutParams = imgMoviePoster.layoutParams
            layoutParams.width = deviceWidth/2
            imgMoviePoster.layoutParams = layoutParams

            Glide.with(itemView).load(imageUrl).error(R.drawable.error_movie).into(imgMoviePoster)
            tvMovieTitle.text = film.title
        }
    }
}