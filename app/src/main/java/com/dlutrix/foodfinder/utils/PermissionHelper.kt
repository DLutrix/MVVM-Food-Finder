package com.dlutrix.foodfinder.utils

import android.Manifest
import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
object PermissionHelper {

    fun hasLocationPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE
        )
}