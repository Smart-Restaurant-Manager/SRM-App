package com.srm.srmapp.data.dto.recipe.response

data class RecipeResponse(
    val data: RecipeData,
){
    fun toRecipe() = data.toRecipe()
}