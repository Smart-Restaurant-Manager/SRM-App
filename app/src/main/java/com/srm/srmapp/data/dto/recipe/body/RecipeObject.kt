package com.srm.srmapp.data.dto.recipe.body

data class RecipeObject(
    val name: String,
    val price: Float,
    val available: Boolean? = null,
    val food: List<RecipeFoodObject>? = null,
)
