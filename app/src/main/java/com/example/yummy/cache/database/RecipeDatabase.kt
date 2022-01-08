package com.example.yummy.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.yummy.cache.RecipeDao
import com.example.yummy.cache.model.RecipeDetailEntity

@Database(entities = [RecipeDetailEntity::class], version = 1)
abstract class RecipeDatabase: RoomDatabase(){

    abstract fun recipeDao(): RecipeDao

}