package com.dlutrix.foodfinder.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.viewpager2.widget.ViewPager2
import com.dlutrix.foodfinder.R
import com.dlutrix.foodfinder.data.model.RestaurantX
import com.dlutrix.foodfinder.databinding.FragmentHomeBinding
import com.dlutrix.foodfinder.ui.core.adapter.RestaurantLoadStateAdapter
import com.dlutrix.foodfinder.ui.home.adapter.CarouselAdapter
import com.dlutrix.foodfinder.ui.home.adapter.RestaurantAroundAdapter
import com.dlutrix.foodfinder.utils.*
import com.dlutrix.foodfinder.utils.Constant.DEFAULT_LAT
import com.dlutrix.foodfinder.utils.Constant.DEFAULT_LONG
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint


/**
 * w0rm1995 on 22/10/20.
 * risfandi@dlutrix.com
 */
@AndroidEntryPoint
class HomeFragment : Fragment(), CarouselAdapter.OnItemClickListener,
    RestaurantAroundAdapter.OnItemClickListener {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var restaurantAroundAdapter: RestaurantAroundAdapter

    private lateinit var sliderHandler: Handler

    private var isLoading = true

    private var currentPosition = 0

    private var shouldMoveForward = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupOnCreateView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        observer()
    }

    private fun setupToolbar() {
        val act = activity as AppCompatActivity?

        with(act) {
            this?.let {
                supportActionBar?.setDisplayShowTitleEnabled(false)
            }
        }

        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_food_bank_24)

        binding.toolbar.inflateMenu(R.menu.menu)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.change_location -> {
                    changeLocation()
                    true
                }
                else -> false
            }
        }

        binding.appbarLayout.addOnOffsetChangedListener(object :
            AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    if (!isLoading) {
                        scrollRange = appBarLayout.totalScrollRange
                    }
                }
                if (scrollRange + verticalOffset == 0) {
                    if (!isShow) {
                        sliderHandler.removeCallbacks(carouselRunnable)
                        isShow = true
                    }
                } else if (isShow) {
                    sliderHandler.postDelayed(carouselRunnable, 3000)
                    isShow = false
                }
            }
        })
    }

    private fun setupOnCreateView() {

        sliderHandler = Handler()

        with(binding) {
            carouselAdapter =
                CarouselAdapter(this@HomeFragment)
            vpCarousel.adapter = carouselAdapter
            restaurantAroundAdapter =
                RestaurantAroundAdapter(this@HomeFragment)
            rvRestaurantAround.setHasFixedSize(true)
            rvRestaurantAround.adapter = restaurantAroundAdapter.withLoadStateFooter(
                RestaurantLoadStateAdapter(restaurantAroundAdapter::retry)
            )
            buttonRetry.setOnClickListener {
                restaurantAroundAdapter.retry()
            }

            restaurantAroundAdapter.addLoadStateListener { loadState ->
                with(binding) {
                    shimmerRestaurantAround.isVisible =
                        loadState.source.refresh is LoadState.Loading
                    rvRestaurantAround.isVisible = loadState.source.refresh is LoadState.NotLoading
                    buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                    textViewError.isVisible = loadState.source.refresh is LoadState.Error
                }
            }
        }
    }

    private fun changeLocation() {
        observe(viewModel.locationLiveData) {
            viewModel.sharedPreferences.edit()
                .putString(Constant.KEY_LAT, it.latitude.toString())
                .putString(Constant.KEY_LONG, it.longitude.toString())
                .apply()

            viewModel.getRemoteData(it.latitude, it.longitude)
        }
    }

    private fun retryObserver() {
        viewModel.sharedPreferences.edit()
            .putString(Constant.KEY_LAT, DEFAULT_LAT)
            .putString(Constant.KEY_LONG, DEFAULT_LONG)
            .apply()

        viewModel.getRemoteData(DEFAULT_LAT.toDouble(), DEFAULT_LONG.toDouble())
    }

    private fun observer() {

        observe(viewModel.locationName) {
            binding.toolbarText.text = it
        }

        observe(viewModel.restaurantAround) {
            restaurantAroundAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        observe(viewModel.restaurantCollection) {
            when (it.status) {
                Status.LOADING -> {
                    with(binding) {
                        vpShimmer.visible()
                        vpCarousel.gone()
                        dotsIndicator.gone()
                        isLoading = true
                    }
                }
                Status.ERROR -> {
                    with(binding) {
                        vpShimmer.gone()
                        vpCarousel.gone()
                        dotsIndicator.gone()
                        rvRestaurantAround.gone()
                        textViewError.gone()
                        buttonRetry.gone()
                    }
                    if (it.isNetworkError!!) {
                        Widget.customDialog(
                            requireContext(),
                            (::retryObserver),
                            "Try Again!",
                            it.message!!,
                            false
                        ).show()
                    } else {
                        Widget.customDialog(
                            requireContext(),
                            (::retryObserver),
                            "Use Default Location",
                            it.message!!,
                            false
                        ).show()
                    }
                }
                Status.SUCCESS -> {
                    carouselAdapter.submitList(it.data!!.collections)
                    with(binding) {
                        vpShimmer.gone()
                        vpCarousel.offscreenPageLimit = 1
                        dotsIndicator.setViewPager2(vpCarousel)
                        vpCarousel.visible()
                        dotsIndicator.visible()
                        vpCarousel.registerOnPageChangeCallback(registerOnPageChangeCallback)
                        isLoading = false
                    }
                }
            }
        }
    }

    private val carouselRunnable = Runnable {
        when {
            currentPosition + 1 == 7 -> {
                shouldMoveForward = false
                binding.vpCarousel.setCurrentItem(binding.vpCarousel.currentItem - 1, true)
            }
            currentPosition == 0 -> {
                shouldMoveForward = true
                binding.vpCarousel.setCurrentItem(binding.vpCarousel.currentItem + 1, true)
            }
            shouldMoveForward -> {
                binding.vpCarousel.setCurrentItem(binding.vpCarousel.currentItem + 1, true)
            }
            !shouldMoveForward -> {
                binding.vpCarousel.setCurrentItem(binding.vpCarousel.currentItem - 1, true)
            }
        }
    }

    private val registerOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            currentPosition = position
            sliderHandler.removeCallbacks(carouselRunnable)
            sliderHandler.postDelayed(carouselRunnable, 3000)
        }
    }

    override fun onCarouselItemClick(collectionId: Int, collectionTitle: String) {
        val action = HomeFragmentDirections
            .actionHomeFragmentToRestaurantByCollectionFragment(
                collectionId,
                collectionTitle
            )
        findNavController().navigate(action)
    }

    override fun onRestaurantItemClick(restaurantX: RestaurantX) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToDetailRestaurantFragment(
                restaurantX
            )
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(carouselRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(carouselRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.vpCarousel.unregisterOnPageChangeCallback(registerOnPageChangeCallback)
        _binding = null
    }
}