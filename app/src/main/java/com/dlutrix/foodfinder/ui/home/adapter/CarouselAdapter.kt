package com.dlutrix.foodfinder.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
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
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    inner class CarouselViewHolder(
        private val binding: ImageSliderItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                val item = differ.currentList[position]
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

    private val differCallback = object : DiffUtil.ItemCallback<Collection>() {
        override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean {
            return oldItem.collection.collectionId == newItem.collection.collectionId
        }

        override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<Collection>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding =
            ImageSliderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = differ.currentList[position]

        holder.bind(item.collection)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnItemClickListener {
        fun onCarouselItemClick(collectionId: Int, collectionTitle: String)
    }

}