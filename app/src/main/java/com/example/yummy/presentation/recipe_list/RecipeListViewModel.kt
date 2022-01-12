package com.example.yummy.presentation.recipe_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.yummy.common.Resource
import com.example.yummy.domain.use_case.get_recipes.GetRecipesUseCase
import androidx.lifecycle.viewModelScope
import com.example.yummy.common.Constants.RECIPE_PAGINATION_PAGE_SIZE
import com.example.yummy.domain.model.RecipeDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val restoreRecipesUseCase: GetRecipesUseCase
): ViewModel() {

    private val _state = mutableStateOf(RecipeListState())
    val state: State<RecipeListState> = _state
    var recipeListScrollPosition = 0

    init {
        getRecipeList()
    }

    private fun getRecipeList() {
        val searchString = "Beef"
        val page = state.value.page
        getRecipesUseCase.invoke(searchString, page).onEach {
            when (it) {
                is Resource.Success -> {
                    appendRecipe(it.data?: emptyList())
                }

                is Resource.Error -> {
                    _state.value = RecipeListState(error = it.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {

                    if (page == 1){
                        _state.value = RecipeListState(isLoading = true)
                    } else {
                        state.value.recipeIncrementLoading = true
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun resetState() {
        _state.value = RecipeListState(recipes = emptyList(), error = "", isLoading = false, page = 1, recipeIncrementLoading = false)
    }

    private fun incrementPage() {
        state.value.page += 1
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        recipeListScrollPosition = position
    }

    private fun appendRecipe(recipes: List<RecipeDetail>) {
        val current = ArrayList(state.value.recipes)
        current.addAll(recipes)
        val page = state.value.page
        _state.value = RecipeListState(recipes = current, page = page)

        if (state.value.recipeIncrementLoading){
            state.value.recipeIncrementLoading  = false
        }
    }

    fun nextPage() {
        viewModelScope.launch {
            if ((recipeListScrollPosition + 1) >= (state.value.page * RECIPE_PAGINATION_PAGE_SIZE) ) {
                state.value.recipeIncrementLoading = true
                incrementPage()
                if (state.value.page > 1){
                    delay(2000)
                    getRecipeList()
                }
            }
        }
    }
}