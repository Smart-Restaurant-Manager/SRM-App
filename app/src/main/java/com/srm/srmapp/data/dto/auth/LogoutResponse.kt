package com.srm.srmapp.data.dto.auth

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("data")
    val data: LogoutData
)