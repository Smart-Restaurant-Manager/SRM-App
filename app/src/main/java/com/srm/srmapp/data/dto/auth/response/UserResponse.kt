package com.srm.srmapp.data.dto.auth.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.srm.srmapp.data.models.User
import java.time.LocalDate

@Keep
data class UserResponse(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: LocalDate,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("role")
    val role: Int,
    @SerializedName("updated_at")
    val updatedAt: LocalDate,
)

fun UserResponse.toUser(): User = User(name, email)