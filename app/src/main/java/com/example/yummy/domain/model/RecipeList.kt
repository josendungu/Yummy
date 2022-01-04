package com.example.yummy.domain.model


data class RecipeList(
    val count: String,
    val next: String,
    val previous: String,
    val results: List<RecipeDetail>
)
