package com.example.yummy.di

import androidx.room.Room
import com.example.yummy.YummyApplication
import com.example.yummy.cache.RecipeDao
import com.example.yummy.cache.database.RecipeDatabase
import com.example.yummy.common.Constants.RECIPE_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideRecipeDatabase(app: YummyApplication): RecipeDatabase{
        return Room
            .databaseBuilder(app, RecipeDatabase::class.java, RECIPE_DATABASE)
            .fallbackToDestructiveMigration()
            .build()
    }


    @Singleton
    @Provides
    fun provideRecipeDao(database: RecipeDatabase): RecipeDao{
        return database.recipeDao()
    }
}