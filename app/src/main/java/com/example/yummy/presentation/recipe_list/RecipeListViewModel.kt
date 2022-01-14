package com.example.yummy.presentation.recipe_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.yummy.common.Resource
import com.example.yummy.domain.use_case.get_recipes.GetRecipesUseCase
import androidx.lifecycle.viewModelScope
import com.example.yummy.common.Constants.RECIPE_PAGINATION_PAGE_SIZE
import com.example.yummy.common.util.DialogQueue
import com.example.yummy.domain.model.RecipeDetail
import com.example.yummy.presentation.util.ConnectivityManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val restoreRecipesUseCase: GetRecipesUseCase,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    private val _state = mutableStateOf(RecipeListState())
    val state: State<RecipeListState> = _state
    var recipeListScrollPosition = 0

    val dialogQueue = DialogQueue()

    init {
        getRecipeList()
    }

    private fun getRecipeList() {
        val searchString = "Beef"
        val page = state.value.page
        val recipes = state.value.recipes

        _state.value = RecipeListState(
            page = page,
            recipes = recipes
        )

        getRecipesUseCase.invoke(
            searchString,
            page,
            connectivityManager.isNetworkAvailable.value
        ).onEach {
            when (it) {
                is Resource.Success -> {
                    appendRecipe(it.data ?: emptyList())
                }

                is Resource.Error -> {

                    if (page == 1) {
                        dialogQueue.appendErrorMessage(
                            title = "Error",
                            description = it.message ?: "An unexpected error occurred"
                        )
                    } else {
                        _state.value = RecipeListState(
                            recipes = recipes,
                            page = page - 1,
                            recipeIncrementError = true
                        )

                    }

                }

                is Resource.Loading -> {

                    if (page == 1) {
                        _state.value = RecipeListState(isLoading = true)
                    } else {
                        _state.value = RecipeListState(
                            page = page,
                            recipes = recipes,
                            recipeIncrementLoading = true
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun resetState() {
        _state.value = RecipeListState(
            recipes = emptyList(),
            error = "",
            isLoading = false,
            page = 1,
            recipeIncrementLoading = false,
            recipeIncrementError = false
        )
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

    }

    fun nextPage() {
        viewModelScope.launch {
            if ((recipeListScrollPosition + 1) >= (state.value.page * RECIPE_PAGINATION_PAGE_SIZE)) {
                incrementPage()
                if (state.value.page > 1) {
                    delay(2000)
                    getRecipeList()
                }
            }
        }
    }
}