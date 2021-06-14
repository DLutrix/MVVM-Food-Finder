package com.dlutrix.foodfinder.ui.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dlutrix.foodfinder.R
import com.dlutrix.foodfinder.databinding.FragmentPermissionBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@AndroidEntryPoint
class PermissionFragment : Fragment() {

    private val viewModel: PermissionViewModel by viewModels()

    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!

    private val requestPermissionLauncher =
        registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.setLocationPermission(isGranted)
            } else {
                viewModel.setLocationPermission(isGranted)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPermissionBinding.inflate(layoutInflater, container, false)

        viewModel.locationPermissionGranted.observe(viewLifecycleOwner) { isGranted ->
            if (isGranted) {
                findNavController().navigate(R.id.action_permissionFragment_to_homeFragment)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.setLocationPermission(true)
        } else {
            viewModel.setLocationPermission(false)
        }
        binding.btnContinue.setOnClickListener {
            requestPermission()
        }
    }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.setLocationPermission(true)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}