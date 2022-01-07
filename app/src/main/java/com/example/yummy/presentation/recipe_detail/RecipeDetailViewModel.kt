package com.example.yummy.presentation.recipe_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import javax.inject.Inject

class RecipeDetailViewModel @Inject constructor(

) {

    private val _state = mutableStateOf(RecipeDetailState())
    val state: State<RecipeDetailState> =  _state

    init {

    }
}