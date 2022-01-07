package com.example.yummy.presentation.recipe_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.yummy.common.Resource
import com.example.yummy.domain.use_case.get_recipes.GetRecipesUseCase
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase
): ViewModel() {

    private val _state = mutableStateOf(RecipeListState())
    val state: State<RecipeListState> = _state

    init {
        getRecipeList()
    }

    private fun getRecipeList() {
        val searchString = "Beef"
        val page = 1
        getRecipesUseCase.invoke(searchString, page).onEach {
            when (it) {
                is Resource.Success -> {
                    _state.value = RecipeListState(recipes = it.data ?: emptyList())

                }

                is Resource.Error -> {
                    _state.value = RecipeListState(error = it.message ?: "An unexpected error occurred")

                }

                is Resource.Loading -> {
                    _state.value = RecipeListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}