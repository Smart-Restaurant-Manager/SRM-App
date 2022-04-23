package com.srm.srmapp.data.dto.stock.response

import androidx.annotation.Keep
import com.srm.srmapp.data.models.Food

@Keep
data class FoodData(
    val attributes: FoodAttributes,
    val id: Int,
    val type: String,
) {
    fun toFood() = Food(type = when (type) {
        else -> ""
    }, foodId = id, name = attributes.name, units = attributes.units)
}