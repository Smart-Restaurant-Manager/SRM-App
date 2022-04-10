package com.srm.srmapp.data.dto.recipe.response

data class RecipeListResponse(
    val data: List<RecipeData>,
) {


        fun toRecipeList() = data.map { it.toRecipe()}
}