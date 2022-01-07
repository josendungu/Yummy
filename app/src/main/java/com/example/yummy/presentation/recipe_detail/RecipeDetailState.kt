package com.example.yummy.presentation.recipe_detail

import com.example.yummy.domain.model.RecipeDetail

data class RecipeDetailState(
    val isLoading: Boolean = false,
    val recipe: RecipeDetail? = null,
    val error: String = ""
)