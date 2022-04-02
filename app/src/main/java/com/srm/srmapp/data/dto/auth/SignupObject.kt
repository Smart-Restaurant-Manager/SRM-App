package com.srm.srmapp.data.dto.auth

data class SignupObject(
    val email: String,
    val name: String,
    val password: String,
    val password_confirmation: String
)