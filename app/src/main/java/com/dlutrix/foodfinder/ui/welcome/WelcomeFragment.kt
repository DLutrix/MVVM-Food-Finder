package com.dlutrix.foodfinder.ui.welcome

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dlutrix.foodfinder.R
import com.dlutrix.foodfinder.databinding.FragmentWelcomeBinding
import com.dlutrix.foodfinder.utils.Constant.KEY_FIRST_TIME
import com.dlutrix.foodfinder.utils.Constant.KEY_LAT
import com.dlutrix.foodfinder.utils.Constant.KEY_LONG
import com.dlutrix.foodfinder.utils.PermissionHelper
import com.dlutrix.foodfinder.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@AndroidEntryPoint
class WelcomeFragment : Fragment(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    private val viewModel: WelcomeViewModel by viewModels()

    @set:Inject
    var isFirstTime = true

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
    }

    private fun requestPermission() {
        if (PermissionHelper.hasLocationPermission(requireContext())) {
            findNavController().navigate(R.id.action_welcomeFragment_to_homeTestAgainFragment)
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this app",
                1,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (!isFirstTime) {
            findNavController().navigate(R.id.action_welcomeFragment_to_homeTestAgainFragment)
        } else {
            observe(viewModel.location) {
                viewModel.sharedPreferences.edit()
                    .putString(KEY_LAT, it.latitude.toString())
                    .putString(KEY_LONG, it.longitude.toString())
                    .putBoolean(KEY_FIRST_TIME, !isFirstTime)
                    .apply()
                findNavController().navigate(R.id.action_welcomeFragment_to_homeTestAgainFragment)
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            requestPermission()
        } else {
            findNavController().navigate(R.id.action_welcomeFragment_to_homeTestAgainFragment)
        }
    }

    override fun onRationaleAccepted(requestCode: Int) {}

    override fun onRationaleDenied(requestCode: Int) {
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}