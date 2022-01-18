package com.example.yummy.cache

import com.example.yummy.cache.model.RecipeDetailEntity

class RecipeDaoFake(
    private val recipeDatabaseFake: RecipeDatabaseFake
): RecipeDao{
    override suspend fun insertRecipe(recipeDetailEntity: RecipeDetailEntity): Long {
        recipeDatabaseFake.recipes.add(recipeDetailEntity)
        return 1 //return success
    }

    override suspend fun insertRecipes(recipes: List<RecipeDetailEntity>): LongArray {
        recipeDatabaseFake.recipes.addAll(recipes)
        return longArrayOf(1)
    }

    override suspend fun getRecipeById(id: Int): RecipeDetailEntity? {
        return recipeDatabaseFake.recipes.find { it.id ==  id }
    }

    override suspend fun deleteRecipes(ids: List<Int>): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllRecipes() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecipe(primaryKey: Int): Int {
        TODO("Not yet implemented")
    }

    override suspend fun searchRecipes(
        query: String,
        page: Int,
        pageSize: Int
    ): List<RecipeDetailEntity> {
        //since we are testing our use cases we only need a list returned from the database
        return recipeDatabaseFake.recipes
    }

    override suspend fun getAllRecipes(page: Int, pageSize: Int): List<RecipeDetailEntity> {
        return recipeDatabaseFake.recipes
    }

    override suspend fun restoreRecipes(
        query: String,
        page: Int,
        pageSize: Int
    ): List<RecipeDetailEntity> {
        return recipeDatabaseFake.recipes
    }

    override suspend fun restoreAllRecipes(page: Int, pageSize: Int): List<RecipeDetailEntity> {
        return recipeDatabaseFake.recipes
    }

}