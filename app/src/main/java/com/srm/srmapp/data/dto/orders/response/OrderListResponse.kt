package com.srm.srmapp.data.dto.orders.response

import com.google.gson.annotations.SerializedName

data class OrderListResponse(
    @SerializedName("data")
    val data: List<OrderResponse>,
)