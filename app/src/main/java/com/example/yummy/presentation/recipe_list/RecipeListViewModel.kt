package com.example.yummy.presentation.recipe_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.yummy.common.Resource
import com.example.yummy.domain.use_case.get_recipes.GetRecipesUseCase
import androidx.lifecycle.viewModelScope
import com.example.yummy.common.Constants.RECIPE_PAGINATION_PAGE_SIZE
import com.example.yummy.common.Constants.STATE_KEY_LIST_POSITION
import com.example.yummy.common.Constants.STATE_KEY_PAGE
import com.example.yummy.common.Constants.STATE_KEY_QUERY
import com.example.yummy.common.util.DialogQueue
import com.example.yummy.domain.model.RecipeDetail
import com.example.yummy.domain.use_case.get_recipes.RestoreRecipesUseCase
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
    private val restoreRecipesUseCase: RestoreRecipesUseCase,
    private val connectivityManager: ConnectivityManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(RecipeListState())
    val state: State<RecipeListState> = _state
    var recipeListScrollPosition = 0
    val textFieldSearchString = mutableStateOf("")
    val dialogQueue = DialogQueue()
    private var restoreState = false

    init {
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let {
            setPage(it)
            restoreState = true
        }
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let {
            setQuery(it)
            restoreState = true
        }
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let {
            setListScrollPosition(it)
            restoreState = true
        }

        if (recipeListScrollPosition != 0){
            restoreState()
        } else {
            getRecipeList()
        }
    }


    private fun restoreState() {
        val searchString = state.value.searchString
        val page = state.value.page

        restoreRecipesUseCase.invoke(
            searchString = searchString,
            page = page
        ).onEach {
            handleResourceResponse(
                response = it,
                page = page,
                recipes = emptyList(),
                searchString = searchString
            )
        }.launchIn(viewModelScope)
    }


    private fun getRecipeList() {
        val searchString = state.value.searchString
        val page = state.value.page
        val recipes = state.value.recipes

        if (page == 1){
            setPage(1)
            setQuery(searchString)
        }

        _state.value = RecipeListState(
            page = page,
            recipes = recipes,
            searchString = searchString
        )

        getRecipesUseCase.invoke(
            searchString,
            page,
            connectivityManager.isNetworkAvailable.value
        ).onEach {
            handleResourceResponse(
                response = it,
                page = page,
                recipes = recipes,
                searchString = searchString
            )
        }.launchIn(viewModelScope)
    }

    private fun handleResourceResponse(
        response: Resource<List<RecipeDetail>>,
        page: Int,
        recipes: List<RecipeDetail>,
        searchString:String
    ) {
        when (response) {
            is Resource.Success -> {
                appendRecipe(response.data ?: emptyList())
            }

            is Resource.Error -> {

                if (page == 1) {
                    dialogQueue.appendErrorMessage(
                        title = "Error",
                        description = response.message ?: "An unexpected error occurred"
                    )
                } else {
                    _state.value = RecipeListState(
                        recipes = recipes,
                        page = page - 1,
                        recipeIncrementError = true,
                        searchString = searchString
                    )

                }

            }

            is Resource.Loading -> {

                if (page == 1) {
                    _state.value = RecipeListState(
                        isLoading = true,
                        searchString = searchString
                    )
                } else {
                    _state.value = RecipeListState(
                        page = page,
                        recipes = recipes,
                        recipeIncrementLoading = true,
                        searchString = searchString
                    )
                }
            }
        }
    }

    private fun incrementPage() {
        val page = state.value.page
        setPage(page+1)
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        setListScrollPosition(position)
    }

    private fun appendRecipe(recipes: List<RecipeDetail>) {
        val current = ArrayList(state.value.recipes)
        current.addAll(recipes)
        val page = state.value.page
        val searchString = state.value.searchString
        _state.value = RecipeListState(recipes = current, page = page, searchString = searchString)

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

    fun toggleSearch() {
        val page = state.value.page
        val recipes = state.value.recipes
        val searchString = state.value.searchString
        val currentDisplayState = state.value.searchDisplayState

        _state.value = RecipeListState(
            recipes = recipes,
            page = page,
            searchString = searchString,
            searchDisplayState = !currentDisplayState
        )
    }

    fun handleSearch() {
        setQuery(textFieldSearchString.value)
        _state.value = RecipeListState(
            searchString = textFieldSearchString.value
        )
        textFieldSearchString.value = ""
        getRecipeList()
    }

    fun onTextStringValueChanged(newString: String) {
        textFieldSearchString.value = newString
    }

    private fun setListScrollPosition(position: Int) {
        recipeListScrollPosition = position
        savedStateHandle.set(STATE_KEY_LIST_POSITION, position)
    }

    private fun setPage(page: Int) {
        savedStateHandle.set(STATE_KEY_PAGE, page)
        state.value.page = page
    }

    private fun setQuery(query: String) {
        savedStateHandle.set(STATE_KEY_QUERY, query)
        state.value.searchString = query
    }
}