package com.srm.srmapp.data.dto.stock.response

data class StockListResponse(
    val data: List<StockData>,
) {
    fun toStockList() = data.map { it.toStock() }
}