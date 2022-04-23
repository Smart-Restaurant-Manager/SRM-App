package com.srm.srmapp.data.dto.stock.response

import androidx.annotation.Keep

@Keep
data class FoodListResponse(
    val data: List<FoodData>,
) {
    fun toFoodList() = data.map { it.toFood() }
}