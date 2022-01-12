package com.example.yummy.presentation.recipe_list

import com.example.yummy.domain.model.RecipeDetail

data class RecipeListState(
    val error: String = "",
    val recipes: List<RecipeDetail> = emptyList(),
    val isLoading: Boolean = false,
    var recipeIncrementLoading: Boolean = false,
    var page: Int = 1
)