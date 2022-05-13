package com.srm.srmapp.data.dto.stock.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.srm.srmapp.data.models.Stock
import java.time.LocalDate

@Keep
data class StockResponse(
    @SerializedName("data")
    val data: Data,
) {
    data class Data(
        @SerializedName("type")
        val type: String, // stocks
        @SerializedName("id")
        val id: Int, // 591
        @SerializedName("attributes")
        val attributes: Attributes,
    ) {
        data class Attributes(
            @SerializedName("quantity")
            val quantity: Int, // 1
            @SerializedName("expiration_date")
            val expirationDate: LocalDate, // 2022-05-10 00:00:00
            @SerializedName("expired")
            val expired: Boolean, // false
            @SerializedName("food_id")
            val foodId: Int, // 831
            @SerializedName("created_at")
            val createdAt: LocalDate, // 2022-05-10T20:48:00.000000Z
            @SerializedName("updated_at")
            val updatedAt: LocalDate, // 2022-05-10T20:48:00.000000Z
        )
    }
}

fun StockResponse.toStock() = data.toStock()
fun StockResponse.Data.toStock() =
    Stock(stockId = id, foodId = attributes.foodId, quantity = attributes.quantity.toFloat(), expirationDate = attributes.expirationDate)
