package com.example.yummy.use_cases.get_recipes

import com.example.yummy.cache.RecipeDaoFake
import com.example.yummy.cache.RecipeDatabaseFake
import com.example.yummy.common.Resource
import com.example.yummy.data.remote.Food2ForkApi
import com.example.yummy.data.repository.Food2ForkRepositoryImpl
import com.example.yummy.domain.model.RecipeDetail
import com.example.yummy.domain.repository.Food2ForkRepository
import com.example.yummy.domain.use_case.get_recipes.GetRecipesUseCase
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

class GetRecipesTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val recipeDatabaseFake = RecipeDatabaseFake()

    private lateinit var getRecipesUseCase: GetRecipesUseCase


    private val dummySearchString = "just Anything"

    //dependencies
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
    }

    @Test
    fun getRecipesFromNetwork_emitRecipesFromCache(): Unit = runBlocking{
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(recipeListResponse)
        )

        //confirm cache is empty
        assert(recipeDaoFake.getAllRecipes(1, 30).isEmpty())

        val flowItems = getRecipesUseCase.invoke(dummySearchString, 1, true).toList()

        //confirm cash is no longer empty
        assert(recipeDaoFake.getAllRecipes(1, 30).isNotEmpty())

        //first emission should be a loading status
        assert(flowItems[0] is Resource.Loading)

        //second emission should be a list of recipes
        val recipes = flowItems[1].data
        assert(recipes?.size?:0 > 0)

        //confirm they are recipe objects
        assert(recipes?.get(index = 0) is RecipeDetail)
        assert(flowItems[1] is Resource.Success )
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
}