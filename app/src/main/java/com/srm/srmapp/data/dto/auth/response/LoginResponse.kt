package com.srm.srmapp.data.dto.auth.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val data: LoginData,
)