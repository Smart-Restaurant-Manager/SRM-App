package com.srm.srmapp.data.models

import com.srm.srmapp.data.dto.stock.body.FoodObject


data class Food(
    val type: String,
    val foodId: Int = -1,
    val name: String,
    val units: String,
    val stockCount: Float = 0f,
    val stockList: MutableList<Stock> = mutableListOf(),
) : GetId {
    override fun getId(): Int {
        return foodId
    }

    override fun toString(): String {
        return "$name $units"
    }

    companion object {
        val TYPES = listOf("Alimentos carnicos", "Lacteos", "Especias", "Vegetales", "Cereales", "Mariscos", "Otros")
    }

    fun toJsonObject(): FoodObject = FoodObject(name, units, type)
}
