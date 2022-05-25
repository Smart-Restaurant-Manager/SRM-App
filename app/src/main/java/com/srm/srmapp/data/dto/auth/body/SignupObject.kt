package com.srm.srmapp.data.dto.auth.body

import androidx.annotation.Keep

@Keep
data class SignupObject(
    val email: String,
    val name: String,
    val role: Int,
    val password: String,
    val password_confirmation: String,
)