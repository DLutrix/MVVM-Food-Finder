package com.dlutrix.foodfinder.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dlutrix.foodfinder.data.model.Collection
import com.dlutrix.foodfinder.data.model.CollectionX
import com.dlutrix.foodfinder.databinding.ImageSliderItemBinding


/**
 * w0rm1995 on 22/10/20.
 * risfandi@dlutrix.com
 */
class CarouselAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Collection, CarouselAdapter.CarouselViewHolder>(DiffCallback()) {

    inner class CarouselViewHolder(
        private val binding: ImageSliderItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                val item = getItem(position)
                listener.onCarouselItemClick(
                    item.collection.collectionId!!,
                    item.collection.title!!
                )
            }
        }


        fun bind(collectionX: CollectionX) {
            with(binding) {
                Glide.with(itemView)
                    .load(collectionX.imageUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)
                tvDescription.text = collectionX.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding =
            ImageSliderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item.collection)
    }

    interface OnItemClickListener {
        fun onCarouselItemClick(collectionId: Int, collectionTitle: String)
    }

    class DiffCallback : DiffUtil.ItemCallback<Collection>() {
        override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean {
            return oldItem.collection.collectionId == newItem.collection.collectionId
        }

        override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean {
            return oldItem == newItem
        }
    }

}