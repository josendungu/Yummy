package com.example.yummy.data.remote

import com.example.yummy.data.remote.dto.RecipeDetailDto
import com.example.yummy.data.remote.dto.RecipeListDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Food2ForkApi {

    @GET("search")
    suspend fun searchRecipe(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): RecipeListDto

    @GET("get")
    suspend fun get(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): RecipeDetailDto

}