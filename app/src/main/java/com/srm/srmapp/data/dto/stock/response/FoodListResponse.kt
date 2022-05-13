package com.srm.srmapp.data.dto.stock.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FoodListResponse(
    @SerializedName("data")
    val data: List<FoodResponse.Data>,
)

fun FoodListResponse.toFoodList() = data.map { it.toFood() }
