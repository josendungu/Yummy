package com.example.yummy.presentation.recipe_list

import com.example.yummy.common.util.DialogQueue
import com.example.yummy.domain.model.RecipeDetail
import com.example.yummy.presentation.components.GenericDialogInfo
import java.util.*

data class RecipeListState(
    val error: String = "",
    val recipes: List<RecipeDetail> = emptyList(),
    val isLoading: Boolean = false,
    var recipeIncrementLoading: Boolean = false,
    var page: Int = 1,
    var recipeIncrementError: Boolean = false,
    var searchString: String = "",
    var searchDisplayState: Boolean = false
)

