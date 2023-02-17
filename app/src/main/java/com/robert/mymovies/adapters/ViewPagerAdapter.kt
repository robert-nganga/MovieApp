package com.robert.mymovies.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.robert.mymovies.R
import com.robert.mymovies.databinding.ImageSliderItemBinding
import com.robert.mymovies.model.Film
import com.robert.mymovies.utils.Constants

class ViewPagerAdapter(): RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding = ImageSliderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val film = differ.currentList[position]
        holder.bind(film)
    }

    override fun getItemCount(): Int = differ.currentList.size
    private val differCallBack = object: DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    inner class ViewPagerViewHolder(private val binding: ImageSliderItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(film: Film) {
            val imageUrl = "${Constants.MOVIE_POSTER_BASE_URL}${film.backdropPath}"
            binding.apply {
                Glide.with(itemView)
                    .load(imageUrl)
                    .error(R.drawable.error_movie)
                    .into(imgCharacter)
                tvListName.text = film.title
            }
        }
    }

}