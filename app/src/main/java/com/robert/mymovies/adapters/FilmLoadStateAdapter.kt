package com.robert.mymovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.robert.mymovies.databinding.FilmListFooterBinding

class FilmLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<FilmLoadStateAdapter.FilmLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: FilmLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FilmLoadStateViewHolder {
        val binding = FilmListFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmLoadStateViewHolder(binding, retry)
    }

    inner class FilmLoadStateViewHolder(
        private val binding: FilmListFooterBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                val errorMsg = "Could not fetch characters at this time"
                binding.errorMsg.text = errorMsg
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }

    }
}