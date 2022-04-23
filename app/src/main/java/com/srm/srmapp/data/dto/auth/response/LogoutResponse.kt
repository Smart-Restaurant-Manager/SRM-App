package com.srm.srmapp.data.dto.auth.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LogoutResponse(
    @SerializedName("data")
    val data: LogoutData,
)