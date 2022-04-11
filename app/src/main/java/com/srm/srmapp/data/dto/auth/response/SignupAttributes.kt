package com.srm.srmapp.data.dto.auth.response

import java.util.*

data class SignupAttributes(
    val email: String,
    val name: String,
    val updated_at: Date,
    val created_at: Date,
)