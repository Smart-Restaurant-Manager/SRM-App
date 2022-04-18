package com.srm.srmapp.data.dto.stock.body

import java.util.*

data class StockObject(
    val quantity: Float,
    val expiration_date: Date,
    val food_id: Int,
    val expired: Boolean? = null,
)