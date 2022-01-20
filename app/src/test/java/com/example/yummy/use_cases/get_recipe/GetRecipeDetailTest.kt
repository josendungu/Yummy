package com.example.yummy.use_cases.get_recipe

import com.example.yummy.cache.RecipeDaoFake
import com.example.yummy.cache.RecipeDatabaseFake
import com.example.yummy.common.Resource
import com.example.yummy.data.remote.Food2ForkApi
import com.example.yummy.data.repository.Food2ForkRepositoryImpl
import com.example.yummy.domain.model.RecipeDetail
import com.example.yummy.domain.repository.Food2ForkRepository
import com.example.yummy.domain.use_case.get_recipe.GetRecipeDetailUseCase
import com.example.yummy.domain.use_case.get_recipes.GetRecipesUseCase
import com.example.yummy.network.data.MockWebServeResponse.recipeDetailResponse1551
import com.example.yummy.network.data.MockWebServeResponse.recipeListResponse
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class GetRecipeDetailTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val recipeDatabaseFake = RecipeDatabaseFake()
    private val dummySearchString = "just Anything"

    // system in test
    private lateinit var getRecipeDetailUseCase: GetRecipeDetailUseCase
    private val RECIPE_ID = 1551


    //dependencies
    private lateinit var getRecipesUseCase: GetRecipesUseCase
    private lateinit var food2ForkApi: Food2ForkApi
    private lateinit var recipeDaoFake: RecipeDaoFake
    private lateinit var food2ForkRepository: Food2ForkRepository


    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/api/recipe/")
        food2ForkApi = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(Food2ForkApi::class.java)

        food2ForkRepository = Food2ForkRepositoryImpl(food2ForkApi)


        recipeDaoFake = RecipeDaoFake(recipeDatabaseFake)
        getRecipesUseCase = GetRecipesUseCase(
            repository = food2ForkRepository,
            recipeDao = recipeDaoFake
        )

        // instantiate the system in test
        getRecipeDetailUseCase = GetRecipeDetailUseCase(
            recipeDao = recipeDaoFake,
            repository = food2ForkRepository
        )
    }


    /**
     * 1. Get some recipes from the network and insert into cache
     * 2. Try to retrieve recipes by their specific recipe id
     */
    @Test
    fun getRecipesFromNetwork_getRecipeById(): Unit = runBlocking {
        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(recipeListResponse)
        )

        // confirm the cache is empty to start
        assert(recipeDaoFake.getAllRecipes(1, 30).isEmpty())

        // get recipes from network and insert into cache
        val searchResult = getRecipesUseCase.invoke(dummySearchString, 1, true).toList()

        // confirm the cache is no longer empty
        assert(recipeDaoFake.getAllRecipes(1, 30).isNotEmpty())

        // run use case
        val recipeAsFlow = getRecipeDetailUseCase.invoke(RECIPE_ID.toString(), true).toList()

        // first emission should be `loading`
        assert(recipeAsFlow[0] is Resource.Loading)

        // second emission should be the recipe
        val recipe = recipeAsFlow[1].data
        assert(recipe?.id == RECIPE_ID)

        // confirm it is actually a Recipe object
        assert(recipe is RecipeDetail)

        // 'loading' should be false now
        assert(recipeAsFlow[1] is Resource.Success)
    }


    /**
     * 1. Try to get a recipe that does not exist in the cache
     * Result should be:
     * 1. Recipe is retrieved from network and inserted into cache
     * 2. Recipe is returned as flow from cache
     */
    @Test
    fun attemptGetNullRecipeFromCache_getRecipeById(): Unit = runBlocking {
        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(recipeDetailResponse1551)
        )

        // confirm the cache is empty to start
        assert(recipeDaoFake.getAllRecipes(1, 30).isEmpty())

        // run use case
        val recipeAsFlow = getRecipeDetailUseCase.invoke(RECIPE_ID.toString(), true).toList()

        // first emission should be `loading`
        assert(recipeAsFlow[0] is Resource.Loading)

        // second emission should be the recipe
        val recipe = recipeAsFlow[1].data
        assert(recipe?.id == RECIPE_ID)

        // confirm the recipe is in the cache now
        assert(recipeDaoFake.getRecipeById(RECIPE_ID)?.id == RECIPE_ID)

        // confirm it is actually a Recipe object
        assert(recipe is RecipeDetail)

        // 'loading' should be false now
        assert(recipeAsFlow[1] is Resource.Success)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
}