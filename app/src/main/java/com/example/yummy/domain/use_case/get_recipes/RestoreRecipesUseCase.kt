package com.example.yummy.domain.use_case.get_recipes

import com.example.yummy.cache.RecipeDao
import com.example.yummy.cache.model.toRecipeDetail
import com.example.yummy.common.Constants
import com.example.yummy.common.Resource
import com.example.yummy.domain.model.RecipeDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RestoreRecipesUseCase @Inject constructor(
    private val recipeDao: RecipeDao
) {

    operator fun invoke(searchString: String, page: Int): Flow<Resource<List<RecipeDetail>>> =
        flow {

           try {

               emit(Resource.Loading())
               val cacheResults = if (searchString.isBlank()){
                   recipeDao.restoreAllRecipes(
                       page = page,
                       pageSize = Constants.RECIPE_PAGINATION_PAGE_SIZE
                   )
               } else {
                   recipeDao.restoreRecipes(
                       query = searchString,
                       pageSize = Constants.RECIPE_PAGINATION_PAGE_SIZE,
                       page = page
                   )
               }

               emit(Resource.Success<List<RecipeDetail>>(cacheResults.map { it.toRecipeDetail() }))

           } catch (e: HttpException) {
               emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
           } catch (e: IOException) {
               emit(Resource.Error("Couldn't reach the server. Check your internet connection"))
           }
        }
}