package com.srm.srmapp.data.dto.stock.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.srm.srmapp.data.models.Food

@Keep
data class FoodResponse(
    @SerializedName("data")
    val data: Data,
) {
    data class Data(
        @SerializedName("type")
        val type: String, // food
        @SerializedName("id")
        val id: Int, // 261
        @SerializedName("attributes")
        val attributes: Attributes,
    ) {
        data class Attributes(
            @SerializedName("name")
            val name: String, // Patatas
            @SerializedName("units")
            val units: String, // Kg
            @SerializedName("type")
            val type: String, // Vegetales
            @SerializedName("created_at")
            val createdAt: String, // 2022-05-04T09:51:05.000000Z
            @SerializedName("updated_at")
            val updatedAt: String, // 2022-05-04T09:51:05.000000Z
        )
    }
}

fun FoodResponse.toFood() = data.toFood()
fun FoodResponse.Data.toFood() = Food(type = attributes.type, foodId = id, name = attributes.name, units = attributes.units)
