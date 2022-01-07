package com.example.yummy.domain.use_case.get_recipe

import androidx.compose.runtime.mutableStateOf
import com.example.yummy.common.Resource
import com.example.yummy.data.remote.dto.toRecipeDetail
import com.example.yummy.domain.model.RecipeDetail
import com.example.yummy.domain.repository.Food2ForkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetRecipeDetailUseCase @Inject constructor(
    private val repository: Food2ForkRepository
) {

    operator fun invoke(id: Int): Flow<Resource<RecipeDetail>> = flow {
        try {
            emit(Resource.Loading())
            val recipe = repository.getRecipeDetail(id).toRecipeDetail()
            emit(Resource.Success<RecipeDetail>(recipe))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach the server. Check your internet connection"))
        }
    }
}