package com.srm.srmapp.data.models

import java.time.LocalDateTime

data class Booking(
    val id: Int? = null,
    val name: String,
    val email: String,
    val phone: String,
    val date: LocalDateTime,
    val people: Int,
    val table: String,
)