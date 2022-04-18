package com.srm.srmapp.data.models

import android.os.Parcelable
import com.srm.srmapp.data.dto.stock.body.StockObject
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Stock(val stockId: Int, val foodId: Int, val quantity: Float, val expirationDate: Date) : Parcelable {
    fun toJsonObject() = StockObject(quantity, expirationDate, foodId, expirationDate < Calendar.getInstance().time)
}