package com.example.yummy.data.remote.dto

import com.example.yummy.common.util.DateUtils
import com.example.yummy.domain.model.RecipeDetail

data class RecipeDetailDto(
    val cooking_instructions: String,
    val date_added: String,
    val date_updated: String,
    val description: String,
    val featured_image: String,
    val ingredients: List<String>,
    val long_date_added: Long,
    val long_date_updated: Long,
    val pk: Int,
    val publisher: String,
    val rating: Int,
    val source_url: String,
    val title: String
)

fun RecipeDetailDto.toRecipeDetail(): RecipeDetail{
    return RecipeDetail(
        featured_image = featured_image,
        ingredients = ingredients,
        id = pk,
        rating = rating,
        title = title,
        date_added = DateUtils.longToDate(long_date_added),
        date_updated = DateUtils.longToDate(long_date_updated)
    )
}