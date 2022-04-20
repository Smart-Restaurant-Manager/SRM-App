package com.srm.srmapp.data.models

import android.os.Parcelable
import com.srm.srmapp.data.dto.stock.body.StockObject
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Stock(val stockId: Int, var foodId: Int, val quantity: Float, val expirationDate: Date) : Parcelable {
    fun toJsonObject() = StockObject(quantity, expirationDate, foodId, expirationDate < Calendar.getInstance().time)
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
        return "$stockId $foodId $quantity $expirationDate"
    }
}