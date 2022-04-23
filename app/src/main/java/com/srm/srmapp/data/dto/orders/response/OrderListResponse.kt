package com.srm.srmapp.data.dto.orders.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OrderListResponse(
    @SerializedName("data")
    val data: List<OrderResponse>,
)