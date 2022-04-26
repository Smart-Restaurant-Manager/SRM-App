package com.srm.srmapp.data.models

import android.os.Parcelable
import com.srm.srmapp.data.dto.stock.body.FoodObject
import kotlinx.parcelize.Parcelize


@Parcelize
data class Food(
    val type: String,
    var foodId: Int = -1,
    val name: String,
    val units: String,
    val stockList: MutableList<Stock> = mutableListOf(),
) :
    Parcelable {
    override fun toString(): String {
        return "$name $units $foodId"
    }

    companion object {
        val TYPES = listOf("Alimentos carnicos", "Lacteos", "Especias", "Vegetales", "Cereales", "Mariscos")
    }

    fun toJsonObject(): FoodObject = FoodObject(name, units, type)
    fun addStock(stock: Stock) {
        stock.foodId = this.foodId
        stockList.add(stock)
    }
}
