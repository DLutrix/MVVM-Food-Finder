package com.dlutrix.foodfinder.ui.restaurantByCollection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.dlutrix.foodfinder.R
import com.dlutrix.foodfinder.data.model.RestaurantX
import com.dlutrix.foodfinder.databinding.FragmentRestaurantByCollectionBinding
import com.dlutrix.foodfinder.ui.core.adapter.RestaurantLoadStateAdapter
import com.dlutrix.foodfinder.ui.restaurantByCollection.adapter.RestaurantByCollectionAdapter
import com.dlutrix.foodfinder.utils.Widget
import com.dlutrix.foodfinder.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@AndroidEntryPoint
class RestaurantByCollectionFragment : Fragment(),
    RestaurantByCollectionAdapter.OnItemClickListener {

    private val args by navArgs<RestaurantByCollectionFragmentArgs>()

    @Inject
    lateinit var viewModelAssistedFactory: RestaurantByCollectionViewModel.AssistedFactory

    private val viewModel: RestaurantByCollectionViewModel by viewModels {
        RestaurantByCollectionViewModel.provideFactory(
            viewModelAssistedFactory, args.collectionId
        )
    }

    private var _binding: FragmentRestaurantByCollectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RestaurantByCollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestaurantByCollectionBinding.inflate(inflater, container, false)

        setupRecycleView()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val act = activity as AppCompatActivity?

        with(act) {
            this?.let {
                setSupportActionBar(binding.toolbar.customToolbar)
                supportActionBar?.setDisplayShowTitleEnabled(false)
            }
        }

        with(binding) {
            toolbar.toolbarText.text = args.collectionTitle
            toolbar.customToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back)
            toolbar.customToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        observer()
    }

    private fun setupRecycleView() {
        adapter = RestaurantByCollectionAdapter(this)
        binding.rvRestaurantItem.setHasFixedSize(true)
        binding.rvRestaurantItem.itemAnimator = null
        binding.rvRestaurantItem.adapter = adapter.withLoadStateFooter(
            RestaurantLoadStateAdapter(adapter::retry)
        )
        adapter.addLoadStateListener { loadState ->
            with(binding) {
                shimmerRestaurantItem.isVisible = loadState.source.refresh is LoadState.Loading
                rvRestaurantItem.isVisible = loadState.source.refresh is LoadState.NotLoading
                if (loadState.source.refresh is LoadState.Error) {
                    Widget.customSnackbar(
                        requireContext(),
                        requireView(),
                       "Result could not be loaded",
                        adapter::retry
                    )
                }
            }

        }
    }

    override fun onRestaurantItemClick(restaurantX: RestaurantX) {
        val action = RestaurantByCollectionFragmentDirections
            .actionRestaurantByCollectionFragmentToDetailRestaurantFragment(restaurantX)
        findNavController().navigate(action)
    }

    private fun observer() {
        observe(viewModel.restaurantByCollection) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}