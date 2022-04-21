package com.srm.srmapp.data.dto.stock.body

import java.time.LocalDate

data class StockObject(
    val quantity: Float,
    val expiration_date: LocalDate,
    val food_id: Int,
    val expired: Boolean? = null,
)