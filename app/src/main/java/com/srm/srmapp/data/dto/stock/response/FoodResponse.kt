package com.srm.srmapp.data.dto.stock.response

data class FoodResponse(
    val data: FoodData,
) {
    fun toFood() = data.toFood()
}