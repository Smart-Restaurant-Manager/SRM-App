package com.srm.srmapp.data.dto.auth.body

import androidx.annotation.Keep

@Keep
data class LoginObject(
    val email: String,
    val password: String,
    val device_name: String,
)