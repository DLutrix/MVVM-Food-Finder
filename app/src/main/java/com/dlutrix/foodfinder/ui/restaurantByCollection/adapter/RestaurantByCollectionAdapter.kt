package com.dlutrix.foodfinder.ui.restaurantByCollection.adapter

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.dlutrix.foodfinder.data.model.Restaurant
import com.dlutrix.foodfinder.data.model.RestaurantX
import com.dlutrix.foodfinder.databinding.RestaurantAroundItemBinding
import java.util.*

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class RestaurantByCollectionAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<Restaurant, RestaurantByCollectionAdapter.RestaurantViewHolder>(
        diffCallback
    ) {

    inner class RestaurantViewHolder(
        private val binding: RestaurantAroundItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    item?.let {
                        listener.onRestaurantItemClick(it.restaurant)
                    }
                }
            }
        }

        fun bind(item: RestaurantX) {
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(16))
            with(binding) {
                Glide.with(itemView)
                    .load(item.featuredImage)
                    .centerCrop()
                    .apply(requestOptions)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)
                name.text = item.name
                dish.text = item.cuisines
                val cost = NumberFormat.getNumberInstance(Locale.getDefault())
                    .format(item.averageCostForTwo)
                val priceText = "${item.currency} $cost for two"
                price.text = priceText
                address.text = item.location.address
                review.text = item.userRating.aggregateRating
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding =
            RestaurantAroundItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item.restaurant)
    }

    companion object {
        private val diffCallback =
            object : DiffUtil.ItemCallback<Restaurant>() {
                override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                    return oldItem.restaurant.r.resId == newItem.restaurant.r.resId
                }

                override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            }
    }

    interface OnItemClickListener {
        fun onRestaurantItemClick(restaurantX: RestaurantX)
    }
}