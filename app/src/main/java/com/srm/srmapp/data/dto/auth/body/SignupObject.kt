package com.srm.srmapp.data.dto.auth.body

data class SignupObject(
    val email: String,
    val name: String,
    val password: String,
    val password_confirmation: String,
)