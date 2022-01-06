package com.example.yummy.presentation

sealed class Screen(val route: String) {
    object RecipeDetailScreen: Screen("recipe_detail_screen")
    object RecipeListScreen: Screen("recipe_list_screen")
}