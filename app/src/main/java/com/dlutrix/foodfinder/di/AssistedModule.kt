package com.dlutrix.foodfinder.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@InstallIn(FragmentComponent::class)
@AssistedModule
@Module(includes = [AssistedInject_AssistedInjectModule::class])
interface AssistedInjectModule