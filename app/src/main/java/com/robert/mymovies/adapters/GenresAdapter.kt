package com.robert.mymovies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robert.mymovies.R
import com.robert.mymovies.model.Genre

class GenresAdapter: RecyclerView.Adapter<GenresAdapter.GenresViewHolder>() {

    private val genres = ArrayList<Genre>()

    fun updateList(newList: List<Genre>){
        genres.clear()
        genres.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        return GenresViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.genre_item, parent, false))
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        val genre = genres[position]
        holder.setData(genre)
    }

    override fun getItemCount(): Int = genres.size

    inner class GenresViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val tvGenre= itemView.findViewById<TextView>(R.id.tvGenre)

        fun setData(genre: Genre){
            tvGenre.text = genre.name
        }
    }
}