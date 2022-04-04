package com.srm.srmapp.data.dto.stock.response

import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.FoodType

data class FoodData(
    val attributes: FoodAttributes,
    val id: Int,
    val type: String,
) {
    fun toFood() = Food(type = when (type) {
        "Carne" -> FoodType.CARNE
        else -> FoodType.NONE
    }, id = id, name = attributes.name, units = attributes.units)
}