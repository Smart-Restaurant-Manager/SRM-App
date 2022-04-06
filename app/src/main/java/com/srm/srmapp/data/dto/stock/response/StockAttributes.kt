package com.srm.srmapp.data.dto.stock.response

import java.util.*

data class StockAttributes(
    val created_at: String,
    val expiration_date: String,
    val expired: Boolean,
    val food_id: Int,
    val quantity: Int,
    val updated_at: String,
)