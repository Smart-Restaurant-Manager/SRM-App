package com.srm.srmapp.data.dto.stock.response

import androidx.annotation.Keep
import com.srm.srmapp.data.models.Stock

@Keep
data class StockData(
    val attributes: StockAttributes,
    val id: Int,
    val type: String,
) {
    fun toStock() =
        Stock(stockId = id, foodId = attributes.food_id, quantity = attributes.quantity.toFloat(), expirationDate = attributes.expiration_date)
}