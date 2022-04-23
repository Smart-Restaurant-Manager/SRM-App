package com.srm.srmapp.data.dto.recipe.response

import androidx.annotation.Keep

@Keep
data class RecipeResponse(
    val data: RecipeData,
)

fun RecipeResponse.toRecipe() = data.toRecipe()
