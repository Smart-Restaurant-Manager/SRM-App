package com.srm.srmapp.data.dto.stock.response

data class StockResponse(
    val data: StockData,
) {
    fun toStock() = data.toStock()
}