package com.dlutrix.foodfinder.ui.detailRestaurant

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.CATEGORY_BROWSABLE
import android.graphics.Color
import android.icu.text.NumberFormat
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import com.bumptech.glide.Glide
import com.dlutrix.foodfinder.R
import com.dlutrix.foodfinder.databinding.FragmentDetailRestaurantBinding
import com.dlutrix.foodfinder.ui.detailRestaurant.adapter.HighlightsAdapter
import com.dlutrix.foodfinder.ui.detailRestaurant.adapter.ReviewAdapter
import com.dlutrix.foodfinder.utils.gone
import com.dlutrix.foodfinder.utils.observe
import com.dlutrix.foodfinder.utils.visible
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@AndroidEntryPoint
class DetailRestaurantFragment : Fragment() {

    private val args by navArgs<DetailRestaurantFragmentArgs>()

    @Inject
    lateinit var viewModelAssistedFactory: DetailRestaurantViewModel.AssistedFactory

    private val viewModel: DetailRestaurantViewModel by viewModels {
        DetailRestaurantViewModel.provideFactory(
            viewModelAssistedFactory,
            args.restaurantX.id.toInt()
        )
    }

    private var _binding: FragmentDetailRestaurantBinding? = null
    private val binding get() = _binding!!

    private lateinit var highlightsAdapter: HighlightsAdapter

    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailRestaurantBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        ViewCompat.setTranslationZ(requireView(), 100f)
        ViewCompat.requestApplyInsets(binding.coordinator)

        setupToolbar()

        with(binding) {
            Glide.with(view)
                .load(args.restaurantX.featuredImage)
                .centerCrop().into(imageView)
            tvName.text = args.restaurantX.name
            tvCuisines.text = args.restaurantX.cuisines
            tvOpeningHour.text = args.restaurantX.location.address
            val cost = NumberFormat.getNumberInstance(Locale.getDefault())
                .format(args.restaurantX.averageCostForTwo)
            val priceText = "${args.restaurantX.currency} $cost for two"
            tvCost.text = priceText
            rating.rating = args.restaurantX.userRating.aggregateRating.toFloat()
        }

        observer()

        binding.bottomNav.selectedItemId = R.id.any

        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.restaurant_menu -> {
                    val uri = Uri.parse(args.restaurantX.menuUrl)
                    val intent = Intent(CATEGORY_BROWSABLE, uri)
                    intent.action = ACTION_VIEW
                    startActivity(intent)
                }
                R.id.location -> {
                    val latitude = args.restaurantX.location.latitude
                    val longitude = args.restaurantX.location.longitude
                    val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude")
                    val mapIntent = Intent(ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)
                }
                R.id.telephone -> {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${args.restaurantX.phoneNumbers}")
                    }
                    startActivity(intent)
                }
            }
            false
        }
    }

    private fun setupRecyclerView() {
        highlightsAdapter = HighlightsAdapter(args.restaurantX.highlights)
        reviewAdapter = ReviewAdapter(viewModel::getReviews)
        val concatAdapter = ConcatAdapter(highlightsAdapter, reviewAdapter)
        binding.rvDetails.setHasFixedSize(true)
        binding.rvDetails.adapter = concatAdapter
    }

    private fun setupToolbar() {
        val act = activity as AppCompatActivity?

        with(act) {
            this?.let {
                supportActionBar?.setDisplayShowTitleEnabled(false)
            }
        }

        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.appbarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    if (binding.toolbarText.visibility == View.GONE) {
                        binding.toolbar.setBackgroundColor(Color.WHITE)
                        binding.toolbarText.text = args.restaurantX.name
                        binding.toolbarText.visible()
                    }
                    isShow = true
                } else if (isShow) {
                    binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
                    binding.toolbarText.gone()
                    isShow = false
                }
            }
        })
    }


    private fun observer() {
        observe(viewModel.reviews) {
            reviewAdapter.submitData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}