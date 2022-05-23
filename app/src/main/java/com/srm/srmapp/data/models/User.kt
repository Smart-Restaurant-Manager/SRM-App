package com.srm.srmapp.data.models

data class User(
    val userId: Int,
    val name: String,
    val email: String,
    val role: Int,
) : GetId {
    override fun getId(): Int {
        return userId
    }
}
