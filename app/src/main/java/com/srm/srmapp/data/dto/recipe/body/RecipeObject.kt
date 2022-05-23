package com.srm.srmapp.data.dto.recipe.body

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RecipeObject(
    @SerializedName("name")
    val name: String, // Hello
    @SerializedName("price")
    val price: Float, // 14
    @SerializedName("type")
    val type: Int, // 1
    @SerializedName("available")
    val available: Boolean, // true
    @SerializedName("food")
    val food: List<Food>,
) {
    data class Food(
        @SerializedName("food_id")
        val foodId: Int, // 3
        @SerializedName("quantity")
        val quantity: Float, // 1
    )
}