package com.srm.srmapp.data.dto.auth.response

import androidx.annotation.Keep
import java.time.LocalDate

@Keep
data class SignupAttributes(
    val email: String,
    val name: String,
    val updated_at: LocalDate,
    val created_at: LocalDate,
)