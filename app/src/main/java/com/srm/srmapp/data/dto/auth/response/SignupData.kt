package com.srm.srmapp.data.dto.auth.response

import androidx.annotation.Keep

@Keep
data class SignupData(
    val attributes: SignupAttributes,
    val id: Int,
    val type: String,
)