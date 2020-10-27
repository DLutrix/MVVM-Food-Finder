package com.dlutrix.foodfinder.di.qualifier

import javax.inject.Qualifier

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class App(val type: Type)