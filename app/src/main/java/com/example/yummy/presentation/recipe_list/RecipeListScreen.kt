package com.example.yummy.presentation.recipe_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.yummy.common.Constants.RECIPE_PAGINATION_PAGE_SIZE
import com.example.yummy.presentation.Screen
import com.example.yummy.presentation.components.RecipeItemComponent
import com.example.yummy.presentation.ui.theme.YummyTheme

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = hiltViewModel(),
    navController: NavController,
    isNetworkAvailable: Boolean,
    isDark: Boolean,
    toggleTheme: () -> Unit
) {

    val dialogQueue = viewModel.dialogQueue

    YummyTheme(
        dialogQueue = dialogQueue.queue.value,
        isNetworkAvailable = isNetworkAvailable,
        darkTheme = isDark
    ) {
        val state = viewModel.state.value
        Content(
            state = viewModel.state.value,
            navController = navController,
            textFieldSearchString = viewModel.textFieldSearchString.value,
            onItemChangePosition = {
                viewModel.onChangeRecipeScrollPosition(it)
            },
            loadNextPage = {
                if ((it + 1) >= (state.page * RECIPE_PAGINATION_PAGE_SIZE) && !state.recipeIncrementLoading) {
                    viewModel.nextPage()
                }
            },
            toggleSearch = {
                viewModel.onTextStringValueChanged("")
                viewModel.toggleSearch()
            },
            onHandleSearch = {viewModel.handleSearch()},
            onValueChange = {
                viewModel.onTextStringValueChanged(it)
            },
            toggleTheme = { toggleTheme() }
        )
    }


}

@Composable
fun Content(
    state: RecipeListState,
    navController: NavController,
    textFieldSearchString: String,
    onItemChangePosition: (index: Int) -> Unit,
    loadNextPage: (index: Int) -> Unit,
    toggleSearch: () -> Unit,
    onValueChange: (String) -> Unit,
    onHandleSearch: () -> Unit,
    toggleTheme: () -> Unit
) {

    val recipes = state.recipes
    val error = state.error
    val loading = state.isLoading
    val incrementError = state.recipeIncrementError

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            
            if (state.searchDisplayState){
                SearchAppBar(
                    searchString = textFieldSearchString,
                    onValueChange = onValueChange,
                    onCloseClicked = { toggleSearch() },
                    onHandleSearch = {onHandleSearch()}
                )
            } else {
                DefaultAppBar(
                    toggleSearch = {toggleSearch()},
                    toggleTheme = {toggleTheme()}
                )

            }
        }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
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

                        if (incrementError) {
                            Text(
                                text = "Error occurred"
                            )
                        }

                        if (!incrementError) {
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


}


@Composable
fun SearchAppBar(
    searchString: String,
    onValueChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onHandleSearch: () -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.primary
    ) {

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchString,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    text = "Search here ...",
                    color = Color.White
                )
            },
            textStyle = MaterialTheme.typography.h5,
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = {},
                    modifier = Modifier.alpha(ContentAlpha.medium)
                ) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (searchString.isNotEmpty()){
                            onValueChange("")
                        } else {
                            onCloseClicked()
                        }
                    },
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close Icon",
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onHandleSearch()
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            )
        )
        
    }

}


@Composable
fun DefaultAppBar(
    toggleSearch: () -> Unit,
    toggleTheme: () -> Unit
){
    TopAppBar(
        title = {
            Text(
                text = "Yummy",
                style = MaterialTheme.typography.h4,
                color = Color.White
            )
        },
        actions = {
            IconButton(onClick = { toggleSearch() }) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }

            IconButton(onClick = { toggleTheme() }) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "More",
                    tint = Color.White
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary
    )
}


