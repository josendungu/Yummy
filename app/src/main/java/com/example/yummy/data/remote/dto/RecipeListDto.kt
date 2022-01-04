package com.example.yummy.data.remote.dto

data class RecipeListDto(
    val count: String,
    val next: String,
    val previous: String,
    val results: List<RecipeDetailDto>
)