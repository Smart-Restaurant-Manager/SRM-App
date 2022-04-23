package com.srm.srmapp.data.dto.stock.body

import androidx.annotation.Keep
import java.time.LocalDate

@Keep
data class StockObject(
    val quantity: Float,
    val expiration_date: LocalDate,
    val food_id: Int,
    val expired: Boolean? = null,
)