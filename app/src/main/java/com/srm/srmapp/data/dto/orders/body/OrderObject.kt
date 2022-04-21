package com.srm.srmapp.data.dto.orders.body


import com.google.gson.annotations.SerializedName

data class OrderObject(
    @SerializedName("booking_id")
    val bookingId: Int, // asperiores
    @SerializedName("recipes")
    val recipes: List<Recipe>,
) {
    data class Recipe(
        @SerializedName("recipe_id")
        val recipeId: String, // eius
        @SerializedName("quantity")
        val quantity: Int, // 17
        @SerializedName("price")
        val price: Double, // 74.0351
        @SerializedName("type")
        val type: Int, // 14
    )
}