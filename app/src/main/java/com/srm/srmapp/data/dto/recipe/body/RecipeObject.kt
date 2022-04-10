package com.srm.srmapp.data.dto.recipe.body

data class RecipeObject(
    val name: String,
    val price: Float,
    val available: Boolean,
    val food: Array<Int>
)
