package com.example.yummy.data.repository

import com.example.yummy.common.Constants
import com.example.yummy.data.remote.Food2ForkApi
import com.example.yummy.data.remote.dto.RecipeDetailDto
import com.example.yummy.data.remote.dto.RecipeListDto
import com.example.yummy.domain.repository.Food2ForkRepository
import javax.inject.Inject

class Food2ForkRepositoryImpl @Inject constructor(
    private val api: Food2ForkApi
): Food2ForkRepository {
    override suspend fun getRecipeList(query: String, page: Int): RecipeListDto {
        return api.searchRecipe(Constants.API_TOKEN, page, query)
    }

    override suspend fun getRecipeDetail(id: Int): RecipeDetailDto {
        return api.get(Constants.API_TOKEN, id)
    }
}