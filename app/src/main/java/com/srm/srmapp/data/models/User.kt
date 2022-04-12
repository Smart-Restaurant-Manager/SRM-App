package com.srm.srmapp.data.models

data class User(
    val name: String,
    val email: String,
) {
    override fun equals(other: Any?): Boolean = other is User && other.name == this.name && other.email == this.email
}
