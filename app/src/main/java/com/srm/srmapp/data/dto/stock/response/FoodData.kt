package com.srm.srmapp.data.dto.stock.response

import com.srm.srmapp.data.models.Food

data class FoodData(
    val attributes: FoodAttributes,
    val id: Int,
    val type: String,
) {
    fun toFood() = Food(type = when (type) {
        "Carne" -> Food.FoodType.CARNE
        else -> Food.FoodType.NONE
    }, id = id, name = attributes.name, units = attributes.units)
}