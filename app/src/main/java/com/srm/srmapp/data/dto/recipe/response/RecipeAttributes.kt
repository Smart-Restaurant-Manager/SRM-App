package com.srm.srmapp.data.dto.recipe.response

import com.srm.srmapp.data.dto.stock.response.FoodData

data class RecipeAttributes(
    val available: Boolean,
    val created_at: String,
    val food: List<FoodData>, // !!! <<----- we already have the response of food
    val image: String,
    val name: String,
    val price: Float,
    val updated_at: String,
)