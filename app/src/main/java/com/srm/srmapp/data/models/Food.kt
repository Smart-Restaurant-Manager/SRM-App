package com.srm.srmapp.data.models

import android.os.Parcelable
import com.srm.srmapp.data.dto.stock.body.FoodObject
import kotlinx.parcelize.Parcelize


@Parcelize
data class Food(
    val type: String,
    val foodId: Int = -1,
    val name: String,
    val units: String,
    val stockCount: Int = 0,
    val stockList: MutableList<Stock> = mutableListOf(),
) : Parcelable, GetId {
    override fun getId(): Int {
        return foodId
    }

    override fun toString(): String {
        return "$name $units $foodId"
    }

    companion object {
        val TYPES = listOf("Alimentos carnicos", "Lacteos", "Especias", "Vegetales", "Cereales", "Mariscos", "Otros")
    }

    fun toJsonObject(): FoodObject = FoodObject(name, units, type)
}
