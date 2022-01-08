package com.example.yummy.presentation.recipe_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.yummy.common.Constants
import com.example.yummy.common.util.loadPicture
import com.example.yummy.domain.model.RecipeDetail
import com.example.yummy.presentation.ui.theme.Red500

@Composable
fun RecipeDetailScreen(
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {

    val state = viewModel.state.value

    Content(recipe = state.recipe, loading = state.isLoading, error = state.error)

}

@Composable
fun Content(
    recipe: RecipeDetail?,
    loading: Boolean,
    error: String
) {

    Box(modifier = Modifier.fillMaxSize()) {

        recipe?.let {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                item {

                    val image = loadPicture(
                        url = recipe.featured_image,
                        defaultImage = Constants.DEFAULT_RECIPE_IMAGE
                    ).value

                    image?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.h3,
                        color = Red500,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 5.dp)
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 5.dp),
                        text = "Cooking Instructions",
                        style = MaterialTheme.typography.h4,
                    )

                }

                items(recipe.ingredients) { instruction ->
                    Text(
                        text = instruction,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
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




