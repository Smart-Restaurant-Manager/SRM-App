package com.srm.srmapp.data.dto.stock.response

import androidx.annotation.Keep
import java.time.LocalDate

@Keep
data class StockAttributes(
    val created_at: LocalDate,
    val expiration_date: LocalDate,
    val expired: Boolean,
    val food_id: Int,
    val quantity: Float,
    val updated_at: LocalDate,
)