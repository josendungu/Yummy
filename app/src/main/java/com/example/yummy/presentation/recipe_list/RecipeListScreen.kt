package com.example.yummy.presentation.recipe_list

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.yummy.common.Constants.TAG
import com.example.yummy.domain.model.RecipeDetail
import com.example.yummy.presentation.Screen
import com.example.yummy.presentation.components.RecipeItemComponent

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = hiltViewModel(),
    navController: NavController
){

    val state = viewModel.state.value
    Content(recipes = state.recipes, navController = navController, error = state.error, loading = state.isLoading)

}

@Composable
fun Content(recipes: List<RecipeDetail>, navController: NavController, error: String, loading:Boolean) {
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(recipes) { recipe ->
                RecipeItemComponent(recipe = recipe, onClick = {
                    navController.navigate(Screen.RecipeDetailScreen.route + "/${recipe.pk}")
                })
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