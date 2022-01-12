package com.example.yummy.presentation.recipe_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.yummy.common.Constants.RECIPE_PAGINATION_PAGE_SIZE
import com.example.yummy.presentation.Screen
import com.example.yummy.presentation.components.RecipeItemComponent
import kotlinx.coroutines.delay

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = hiltViewModel(),
    navController: NavController
) {

    val state = viewModel.state.value
    Content(
        state = viewModel.state.value,
        navController = navController,
        onItemChangePosition = {
            viewModel.onChangeRecipeScrollPosition(it)
        },
        loadNextPage = {
            if ((it + 1) >= (state.page * RECIPE_PAGINATION_PAGE_SIZE) && !state.recipeIncrementLoading) {
                viewModel.nextPage()
            }
        }
    )

}

@Composable
fun Content(
    state: RecipeListState,
    navController: NavController,
    onItemChangePosition: (index: Int) -> Unit,
    loadNextPage: (index: Int) -> Unit,
) {

    val recipes = state.recipes
    val error = state.error
    val loading = state.isLoading

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(items = recipes) { index, recipe ->
                onItemChangePosition(index)
                loadNextPage(index)
                RecipeItemComponent(recipe = recipe, onClick = {
                    navController.navigate(Screen.RecipeDetailScreen.route + "/${recipe.id}")
                })
            }

            if (!loading) {
                item {

                    Box(modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .height(13.dp)
                                .width(13.dp),
                            strokeWidth = 2.dp
                        )
                    }


                }
            }

        }
        if (error.isNotBlank()) {
            Text(
                text = error,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )
        }
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

    }

}