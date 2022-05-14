package com.srm.srmapp.data.dto.stock.body

import androidx.annotation.Keep

@Keep
data class FoodObject(
    val name: String,
    val units: String,
    val type: String,
)