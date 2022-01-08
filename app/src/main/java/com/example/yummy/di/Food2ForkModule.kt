package com.example.yummy.di

import com.example.yummy.common.Constants
import com.example.yummy.data.remote.Food2ForkApi
import com.example.yummy.data.repository.Food2ForkRepositoryImpl
import com.example.yummy.domain.repository.Food2ForkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Food2ForkModule {

    @Provides
    @Singleton
    fun provideFood2ForkApi(): Food2ForkApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Food2ForkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFood2ForkRepository(api: Food2ForkApi): Food2ForkRepository {
        return Food2ForkRepositoryImpl(api)
    }
}