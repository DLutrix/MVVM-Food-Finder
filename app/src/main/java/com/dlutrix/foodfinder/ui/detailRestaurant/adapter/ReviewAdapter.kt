package com.dlutrix.foodfinder.ui.detailRestaurant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dlutrix.foodfinder.data.model.Review
import com.dlutrix.foodfinder.databinding.ReviewBinding
import com.dlutrix.foodfinder.utils.Resource
import com.dlutrix.foodfinder.utils.Status
import com.dlutrix.foodfinder.utils.gone
import com.dlutrix.foodfinder.utils.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * w0rm1995 on 17/10/20.
 * risfandi@dlutrix.com
 */
class ReviewAdapter(private val retry: () -> Unit) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private var data: Resource<Review>? = null

    private val reviewItemAdapter: ReviewItemAdapter = ReviewItemAdapter()

    fun submitData(item: Resource<Review>) {
        data = item
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(
        private val binding: ReviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind() {
            CoroutineScope(Dispatchers.Main).launch {
                delay(1500L)
                when (data?.status) {
                    Status.LOADING -> {
                        with(binding) {
                            shimmer.visible()
                            rvReview.gone()
                            textViewError.gone()
                            buttonRetry.gone()
                        }
                    }
                    Status.ERROR -> {
                        with(binding) {
                            shimmer.gone()
                            rvReview.gone()
                            textViewError.text = data!!.message
                            textViewError.visible()
                            buttonRetry.visible()
                            buttonRetry.setOnClickListener {
                                retry.invoke()
                            }
                        }
                    }
                    Status.SUCCESS -> {
                        with(binding) {
                            rvReview.adapter = reviewItemAdapter
                            reviewItemAdapter.submitList(data!!.data!!.userReviews)
                            buttonRetry.gone()
                            shimmer.gone()
                            textViewError.gone()
                            rvReview.visible()
                        }
                    }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 1
    }
}