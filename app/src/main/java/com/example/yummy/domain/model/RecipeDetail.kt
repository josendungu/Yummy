package com.example.yummy.domain.model

data class RecipeDetail(
    val description: String,
    val featured_image: String,
    val ingredients: List<String>,
    val pk: Int,
    val rating: Int,
    val title: String
)
