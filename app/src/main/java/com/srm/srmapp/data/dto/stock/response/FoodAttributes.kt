package com.srm.srmapp.data.dto.stock.response

import java.time.LocalDate

data class FoodAttributes(
    val created_at: LocalDate,
    val name: String,
    val units: String,
    val updated_at: LocalDate,
)