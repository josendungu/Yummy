package com.example.yummy.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeDetailEntity(

    @ColumnInfo(name = "featured_image")
    val featured_image: String,

    @ColumnInfo(name = "ingredients")
    val ingredients: String,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val pk: Int,

    @ColumnInfo(name = "rating")
    val rating: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "date_added")
    val date_added: Long,

    @ColumnInfo(name = "date_updated")
    val date_updated: Long,

    @ColumnInfo(name = "date_refreshed")
    val date_refreshed: Long
)