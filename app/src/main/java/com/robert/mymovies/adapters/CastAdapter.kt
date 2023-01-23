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
import com.robert.mymovies.databinding.CastItemBinding
import com.robert.mymovies.databinding.TrendingItemBinding
import com.robert.mymovies.model.Cast
import com.robert.mymovies.utils.Constants

class CastAdapter(): RecyclerView.Adapter<CastAdapter.CastViewHolder>() {


    private val differCallBack = object: DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }
    }
    private var onItemClickListener: ((Cast)->Unit)? = null

    fun setOnItemClickListener(listener: (Cast)-> Unit){
        onItemClickListener = listener
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = CastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = differ.currentList[position]
        holder.itemView.setOnClickListener { onItemClickListener?.let { it(cast) }}
        holder.setData(cast)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class CastViewHolder(private val binding: CastItemBinding): RecyclerView.ViewHolder(binding.root) {
        private var currentCast: Cast? = null

        fun setData(cast: Cast){
            currentCast = cast
            val imageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${cast.profile_path}"

            Glide.with(itemView).load(imageUrl).error(R.drawable.error_profile).into(binding.imgCast)
            binding.tvCastName.text = cast.name
            binding.tvCastCharacter.text = cast.character
        }
    }
}