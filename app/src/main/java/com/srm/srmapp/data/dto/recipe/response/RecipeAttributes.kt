package com.srm.srmapp.data.dto.recipe.response

import androidx.annotation.Keep
import com.srm.srmapp.data.dto.stock.response.FoodData

@Keep
data class RecipeAttributes(
    val available: Boolean,
    val created_at: String,
    val food: List<FoodData>, // !!! <<----- we already have the response of food
    val image: String,
    val name: String,
    val type: Int,
    val price: Float,
    val updated_at: String,
)