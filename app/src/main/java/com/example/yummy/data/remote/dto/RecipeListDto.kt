package com.example.yummy.data.remote.dto

import com.example.yummy.domain.model.RecipeList

data class RecipeListDto(
    val count: String,
    val next: String,
    val previous: String,
    val results: List<RecipeDetailDto>
)


fun RecipeListDto.toRecipeList(): RecipeList {
    return RecipeList(
        count = count,
        next = next,
        previous = previous,
        results = results.map {
            it.toRecipeDetail()
        }
    )
}