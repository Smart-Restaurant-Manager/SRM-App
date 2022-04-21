package com.srm.srmapp.data.dto.auth.response

import java.time.LocalDate
import java.util.*

data class SignupAttributes(
    val email: String,
    val name: String,
    val updated_at: LocalDate,
    val created_at: LocalDate,
)