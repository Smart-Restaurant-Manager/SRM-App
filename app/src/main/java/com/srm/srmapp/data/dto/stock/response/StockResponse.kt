package com.srm.srmapp.data.dto.stock.response

import androidx.annotation.Keep

@Keep
data class StockResponse(
    val data: StockData,
) {
    fun toStock() = data.toStock()
}