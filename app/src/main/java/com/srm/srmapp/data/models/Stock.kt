package com.srm.srmapp.data.models

import com.srm.srmapp.data.dto.stock.body.StockObject
import java.time.LocalDate

data class Stock(val stockId: Int, val foodId: Int, val quantity: Float, val expirationDate: LocalDate) : GetId {
    fun toJsonObject() = StockObject(quantity, expirationDate, foodId, null)
    override fun getId(): Int {
        return stockId
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Stock) return false
        return other.foodId == foodId && other.expirationDate == expirationDate
    }

    override fun hashCode(): Int {
        var result = stockId
        result = 31 * result + foodId
        result = 31 * result + quantity.hashCode()
        result = 31 * result + expirationDate.hashCode()
        return result
    }

    override fun toString(): String {
        return "Id $stockId Quantitat: $quantity Caducitat: $expirationDate"
    }
}