package com.robert.mymovies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robert.mymovies.R
import com.robert.mymovies.databinding.GenreItemBinding
import com.robert.mymovies.databinding.TrendingItemBinding
import com.robert.mymovies.model.Genre

class GenresAdapter: RecyclerView.Adapter<GenresAdapter.GenresViewHolder>() {

    private val genres = ArrayList<Genre>()

    fun updateList(newList: List<Genre>){
        genres.clear()
        genres.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val binding = GenreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenresViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        val genre = genres[position]
        holder.setData(genre)
    }

    override fun getItemCount(): Int = genres.size

    inner class GenresViewHolder(private val binding: GenreItemBinding): RecyclerView.ViewHolder(binding.root){

        fun setData(genre: Genre){
            binding.tvGenre.text = genre.name
        }
    }
}