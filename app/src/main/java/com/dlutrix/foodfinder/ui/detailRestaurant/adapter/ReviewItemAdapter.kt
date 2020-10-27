package com.dlutrix.foodfinder.ui.detailRestaurant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.dlutrix.foodfinder.data.model.ReviewX
import com.dlutrix.foodfinder.data.model.UserReview
import com.dlutrix.foodfinder.databinding.ReviewItemBinding


/**
 * w0rm1995 on 17/10/20.
 * risfandi@dlutrix.com
 */
class ReviewItemAdapter : RecyclerView.Adapter<ReviewItemAdapter.ReviewItemViewHolder>() {


    class ReviewItemViewHolder(
        private val binding: ReviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private fun expandText(textView: TextView) {
            if (textView.maxLines != Integer.MAX_VALUE && textView.ellipsize != null) {
                textView.maxLines = Integer.MAX_VALUE
                textView.ellipsize = null
            }
        }


        init {
            binding.root.setOnClickListener {
                expandText(binding.tvReview)
            }
        }

        fun bind(item: ReviewX) {
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(16))
            with(binding) {
                Glide.with(itemView)
                    .load(item.user.profileImage)
                    .centerCrop()
                    .apply(requestOptions)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivUser)
                tvName.text = item.user.name
                tvTime.text = item.reviewTimeFriendly
                rating.rating = item.rating.toFloat()
                tvReview.text = item.reviewText
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewItemViewHolder {
        val binding = ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ReviewItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewItemViewHolder, position: Int) {
        val item = differ.currentList[position]

        holder.bind(item.review)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<UserReview>() {
        override fun areItemsTheSame(oldItem: UserReview, newItem: UserReview): Boolean {
            return oldItem.review.id == newItem.review.id
        }

        override fun areContentsTheSame(oldItem: UserReview, newItem: UserReview): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<UserReview>) = differ.submitList(list)
}