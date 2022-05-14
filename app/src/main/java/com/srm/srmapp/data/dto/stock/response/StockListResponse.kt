package com.srm.srmapp.data.dto.stock.response

import androidx.annotation.Keep

@Keep
data class StockListResponse(
    val data: List<StockResponse.Data>,
)

fun StockListResponse.toStockList() = data.map { it.toStock() }
