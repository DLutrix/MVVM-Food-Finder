package com.dlutrix.foodfinder.ui.detailRestaurant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dlutrix.foodfinder.databinding.HighlightsBinding


/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class HighlightsAdapter(private val item: List<String>) :
    RecyclerView.Adapter<HighlightsAdapter.HighlightsViewHolder>() {

    private lateinit var adapter: HighlightsChipItemAdapter

    inner class HighlightsViewHolder(
        private val binding: HighlightsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            adapter = HighlightsChipItemAdapter(item)
        }

        fun bind() {
            binding.rvChipItem.adapter = adapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighlightsViewHolder {
        val binding = HighlightsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HighlightsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HighlightsViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 1
    }
}