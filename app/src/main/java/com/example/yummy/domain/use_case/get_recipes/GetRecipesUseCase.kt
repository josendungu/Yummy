package com.example.yummy.domain.use_case.get_recipes

import com.example.yummy.common.Resource
import com.example.yummy.data.remote.dto.toRecipeDetail
import com.example.yummy.domain.model.RecipeDetail
import com.example.yummy.domain.model.RecipeList
import com.example.yummy.domain.repository.Food2ForkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val repository: Food2ForkRepository
) {

    operator fun invoke(searchString: String, page: Int): Flow<Resource<List<RecipeDetail>>> =
        flow {
            try {
                emit(Resource.Loading())
                val recipe = repository.getRecipeList(searchString, page).results.map {
                    it.toRecipeDetail()
                }
                emit(Resource.Success<List<RecipeDetail>>(recipe))

            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach the server. Check your internet connection"))
            }
        }
}
