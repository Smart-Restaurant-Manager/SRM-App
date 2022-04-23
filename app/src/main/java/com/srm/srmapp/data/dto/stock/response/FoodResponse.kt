package com.srm.srmapp.data.dto.stock.response

import androidx.annotation.Keep

@Keep
data class FoodResponse(
    val data: FoodData,
) {
    fun toFood() = data.toFood()
}