package com.srm.srmapp.data.dto.recipe.response

data class RecipeListResponse(
    val data: List<RecipeData>,
)

fun RecipeListResponse.toRecipeList() = data.map { it.toRecipe() }
