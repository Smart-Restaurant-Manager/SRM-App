package com.srm.srmapp.data.dto.orders.body


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OrderObject(
    @SerializedName("order_status_id")
    val orderStatus: Int, // 1
    @SerializedName("booking_id")
    val bookingId: Int, // asperiores
    @SerializedName("recipes")
    val recipes: List<Recipe>,
) {
    data class Recipe(
        @SerializedName("recipe_id")
        val recipeId: Int, // eius
        @SerializedName("quantity")
        val quantity: Float, // 17
        @SerializedName("price")
        val price: Float, // 74.0351
    )
}
