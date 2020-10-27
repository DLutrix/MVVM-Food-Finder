package com.dlutrix.foodfinder.utils

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, { it?.let { t -> action(t) } })
}