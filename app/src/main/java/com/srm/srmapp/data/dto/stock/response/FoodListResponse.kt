package com.srm.srmapp.data.dto.stock.response

data class FoodListResponse(
    val data: List<FoodData>,
) {
    fun toFoodList() = data.map { it.toFood() }
}