package com.dlutrix.foodfinder.ui.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dlutrix.foodfinder.databinding.LoadStateFooterBinding

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class RestaurantLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<RestaurantLoadStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(
        private val binding: LoadStateFooterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            with(binding) {
                shimmer.isVisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState !is LoadState.Loading
                textViewError.isVisible = loadState !is LoadState.Loading
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LoadStateFooterBinding.inflate(LayoutInflater.from(parent.context))

        return LoadStateViewHolder(binding).also {
            binding.buttonRetry.setOnClickListener {
                retry.invoke()
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {

        holder.bind(loadState)
    }
}