package com.example.yummy.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yummy.presentation.recipe_detail.RecipeDetailScreen
import com.example.yummy.presentation.recipe_list.RecipeListScreen
import com.example.yummy.presentation.ui.theme.YummyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YummyTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.RecipeListScreen.route
                    ){

                        composable(
                            route = Screen.RecipeListScreen.route
                        ) {
                            RecipeListScreen(navController = navController)
                        }

                        composable(
                            route = Screen.RecipeDetailScreen.route
                        ) {
                            RecipeDetailScreen()
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    YummyTheme {
        Greeting("Android")
    }
}