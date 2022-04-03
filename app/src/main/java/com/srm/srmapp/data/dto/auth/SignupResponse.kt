package com.srm.srmapp.data.dto.auth

import com.google.gson.annotations.SerializedName

data class SignupResponse(
    @SerializedName("data")
    val data: SignupData,
)