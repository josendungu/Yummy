package com.example.yummy.domain.repository

import com.example.yummy.data.remote.dto.RecipeDetailDto
import com.example.yummy.data.remote.dto.RecipeListDto

interface Food2ForkRepository {

    suspend fun getRecipeList(query: String, page: Int): RecipeListDto

    suspend fun getRecipeDetail(id: String): RecipeDetailDto

}