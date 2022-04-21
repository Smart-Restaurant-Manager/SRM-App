package com.srm.srmapp.data.models

import android.os.Parcelable
import com.srm.srmapp.data.dto.stock.body.StockObject
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Stock(val stockId: Int, var foodId: Int, val quantity: Float, val expirationDate: LocalDate) : Parcelable {
    fun toJsonObject() = StockObject(quantity, expirationDate, foodId, null)
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