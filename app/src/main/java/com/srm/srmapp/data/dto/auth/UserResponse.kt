package com.srm.srmapp.data.dto.auth

import com.srm.srmapp.data.models.User
import java.util.*

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val email_verified_at: Date?,
    val created_at: Date,
    val updated_at: Date,
)

fun UserResponse.toUser(): User = User(name, email)