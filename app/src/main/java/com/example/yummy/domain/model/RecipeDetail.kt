package com.example.yummy.domain.model

import com.example.yummy.cache.model.RecipeDetailEntity
import com.example.yummy.common.util.DateUtils
import com.example.yummy.common.util.ListStringConversion
import java.util.*

data class RecipeDetail(
    val featured_image: String,
    val ingredients: List<String>,
    val id: Int,
    val rating: Int,
    val title: String,
    val date_added: Date,
    val date_updated: Date,
)

fun RecipeDetail.toRecipeDetailEntity(): RecipeDetailEntity {
    return RecipeDetailEntity(
        featured_image = featured_image,
        id = id,
        rating = rating,
        title = title,
        date_added = DateUtils.dateToLong(date_added),
        date_updated = DateUtils.dateToLong(date_updated),
        ingredients = ListStringConversion.listToString(ingredients),
        date_refreshed = DateUtils.dateToLong(DateUtils.createTimestamp())
    )
}
