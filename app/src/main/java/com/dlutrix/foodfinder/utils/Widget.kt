package com.dlutrix.foodfinder.utils

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dlutrix.foodfinder.R
import com.google.android.material.snackbar.Snackbar

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
object Widget {

    fun customSnackbar(
        context: Context,
        view: android.view.View,
        message: String,
        action: () -> Unit
    ) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
        val snackbarActionTextView =
            snackbar.view.findViewById(com.google.android.material.R.id.snackbar_action) as TextView
        snackbarActionTextView.isAllCaps = false
        snackbar.view.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.secondaryColorAccent
            )
        )
        snackbar.setActionTextColor(Color.WHITE)
        snackbar.setAction("Retry") {
            action()
        }
        snackbar.show()
    }
}
