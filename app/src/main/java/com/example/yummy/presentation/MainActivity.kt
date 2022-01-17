package com.example.yummy.presentation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yummy.datastore.SettingsDataStore
import com.example.yummy.presentation.recipe_detail.RecipeDetailScreen
import com.example.yummy.presentation.recipe_list.RecipeListScreen
import com.example.yummy.presentation.ui.theme.YummyTheme
import com.example.yummy.presentation.util.ConnectivityManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    override fun onStart() {
        super.onStart()
        connectivityManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterConnectionObserver(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            // A surface container using the 'background' color from the theme
            Surface(color = MaterialTheme.colors.background) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.RecipeListScreen.route
                ) {

                    composable(
                        route = Screen.RecipeListScreen.route
                    ) {
                        RecipeListScreen(
                            navController = navController,
                            isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                            isDark = settingsDataStore.isDark.value,
                            toggleTheme = settingsDataStore::toggleTheme
                        )
                    }

                    composable(
                        route = Screen.RecipeDetailScreen.route + "/{recipeId}"
                    ) {
                        RecipeDetailScreen(
                            isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                            isDark = settingsDataStore.isDark.value
                        )
                    }

                }
            }


        }
    }
}