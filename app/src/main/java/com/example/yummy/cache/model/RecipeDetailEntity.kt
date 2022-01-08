package com.example.yummy.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.yummy.common.util.DateUtils
import com.example.yummy.common.util.ListStringConversion
import com.example.yummy.domain.model.RecipeDetail

@Entity(tableName = "recipes")
data class RecipeDetailEntity(

    @ColumnInfo(name = "featured_image")
    val featured_image: String,

    @ColumnInfo(name = "ingredients")
    val ingredients: String,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,

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

fun RecipeDetailEntity.toRecipeDetail(): RecipeDetail {
    return RecipeDetail(
        featured_image = featured_image,
        ingredients = ListStringConversion.stringToList(ingredients),
        id = id,
        rating = rating,
        title = title,
        date_added = DateUtils.longToDate(date_added),
        date_updated = DateUtils.longToDate(date_updated)
    )
}