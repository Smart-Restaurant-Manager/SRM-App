package com.srm.srmapp.data.dto.auth.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SignupResponse(
    @SerializedName("data")
    val data: SignupData,
)