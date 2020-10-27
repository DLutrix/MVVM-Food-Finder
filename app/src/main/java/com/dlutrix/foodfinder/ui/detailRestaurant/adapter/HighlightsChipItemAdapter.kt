package com.dlutrix.foodfinder.ui.detailRestaurant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dlutrix.foodfinder.databinding.HighlightsChipItemBinding


/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class HighlightsChipItemAdapter(private val items: List<String>) :
    RecyclerView.Adapter<HighlightsChipItemAdapter.ChipViewHolder>() {

    class ChipViewHolder(
        private val binding: HighlightsChipItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.chipItem.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val binding = HighlightsChipItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}