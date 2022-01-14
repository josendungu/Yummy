package com.example.yummy.presentation.recipe_detail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.yummy.common.Constants
import androidx.lifecycle.viewModelScope
import com.example.yummy.common.Constants.TAG
import com.example.yummy.common.Resource
import com.example.yummy.common.util.DialogQueue
import com.example.yummy.domain.use_case.get_recipe.GetRecipeDetailUseCase
import com.example.yummy.presentation.util.ConnectivityManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getRecipeDetailUseCase: GetRecipeDetailUseCase,
    savedStateHandle: SavedStateHandle,
    private val connectivityManager: ConnectivityManager
): ViewModel() {

    private val _state = mutableStateOf(RecipeDetailState())
    val state: State<RecipeDetailState> =  _state

    val dialogQueue = DialogQueue()

    init {
        savedStateHandle.get<String>(Constants.PARAM_RECIPE_ID)?.let {
            getRecipeDetails(it)
        }
    }

    private fun getRecipeDetails(recipeId: String) {
        getRecipeDetailUseCase.invoke(
            recipeId,
            connectivityManager.isNetworkAvailable.value
        ).onEach {
            when (it) {
                is Resource.Success -> {
                    _state.value = RecipeDetailState(recipe = it.data)

                }

                is Resource.Error -> {
                    //_state.value = RecipeDetailState(error = it.message ?: "An unexpected error occurred")
                    dialogQueue.appendErrorMessage("Error", it.message ?: "An unexpected error occurred")

                }

                is Resource.Loading -> {
                    _state.value = RecipeDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}