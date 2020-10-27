package com.dlutrix.foodfinder.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.dlutrix.foodfinder.data.source.ZomatoApiService
import com.dlutrix.foodfinder.di.qualifier.App
import com.dlutrix.foodfinder.di.qualifier.Type
import com.dlutrix.foodfinder.utils.Constant
import com.dlutrix.foodfinder.utils.Constant.KEY_FIRST_TIME
import com.dlutrix.foodfinder.utils.Constant.KEY_LAT
import com.dlutrix.foodfinder.utils.Constant.KEY_LONG
import com.dlutrix.foodfinder.utils.Constant.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    @App(Type.BASE_URL)
    fun provideBaseUrl() = Constant.ZOMATO_BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient() =
        if (Constant.DEBUG) {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            OkHttpClient.Builder()
                .addInterceptor(logger)
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .build()
        } else
            OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @App(Type.BASE_URL) baseUrl: String,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit
    ): ZomatoApiService = retrofit.create(ZomatoApiService::class.java)


    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideFirstTimeToggle(sharedPreferences: SharedPreferences) =
        sharedPreferences.getBoolean(KEY_FIRST_TIME, true)
}