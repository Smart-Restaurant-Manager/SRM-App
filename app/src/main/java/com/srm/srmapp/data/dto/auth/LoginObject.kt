package com.srm.srmapp.data.dto.auth

data class LoginObject(
    val email: String,
    val password: String,
    val device_name: String,
)