package com.dlutrix.foodfinder.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

/**
 * w0rm1995 on 22/10/20.
 * risfandi@dlutrix.com
 */
@ExperimentalCoroutinesApi
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

    private val requestPermissionLauncher =
        registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.refreshLocation()
            }
        }

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

        sliderHandler = Handler(Looper.getMainLooper())

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
                    rvRestaurantAround.isVisible =
                        loadState.source.refresh is LoadState.NotLoading && viewModel.isError.value == false
                    buttonRetry.isVisible =
                        loadState.source.refresh is LoadState.Error && viewModel.isError.value == false
                    textViewError.isVisible =
                        loadState.source.refresh is LoadState.Error && viewModel.isError.value == false
                }
            }
        }
    }

    private fun changeLocation() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.refreshLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Snackbar.make(
                    requireView(),
                    "You need to accept location permission to use this app",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Settings") {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", requireContext().packageName, null)
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }.show()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun observer() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.homeEvent.collect {
                when (it) {
                    is HomeViewModel.HomeEvent.RefreshLocation -> {
                        viewModel.retryObserverWhenLocationChanged(
                            it.location.first.toDouble(),
                            it.location.second.toDouble()
                        )
                    }
                    is HomeViewModel.HomeEvent.NavigateToDetailRestaurant -> {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToDetailRestaurantFragment(
                                it.restaurantX
                            )
                        findNavController().navigate(action)
                    }
                    is HomeViewModel.HomeEvent.NavigateToRestaurantCollection -> {
                        val action = HomeFragmentDirections
                            .actionHomeFragmentToRestaurantByCollectionFragment(
                                it.collectionId,
                                it.collectionTitle
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }

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
                        viewModel.setIsError(true)
                        vpShimmer.gone()
                        vpCarousel.gone()
                        dotsIndicator.gone()
                        rvRestaurantAround.gone()
                        textViewError.gone()
                        buttonRetry.gone()
                    }
                    if (it.isNetworkError!!) {
                        Widget.customSnackbar(
                            requireContext(),
                            requireView(),
                            it.message!!,
                            ((viewModel::retryObserver))
                        )
                    } else {
                        Widget.customSnackbar(
                            requireContext(),
                            requireView(),
                            it.message!!,
                            ((viewModel::retryObserverIfLocationNotAvailable))
                        )
                    }
                }
                Status.SUCCESS -> {
                    carouselAdapter.submitList(it.data!!.collections)
                    with(binding) {
                        viewModel.setIsError(false)
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
        viewModel.onRestaurantCollectionClick(collectionId, collectionTitle)
    }

    override fun onRestaurantItemClick(restaurantX: RestaurantX) {
        viewModel.onRestaurantItemClick(restaurantX)
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