package com.srm.srmapp.data.dto.bookings.body


import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class BookingObject(
    @SerializedName("name")
    val name: String, // a
    @SerializedName("email")
    val email: String, // a@a.a
    @SerializedName("phone")
    val phone: String, // 1
    @SerializedName("date")
    val date: LocalDateTime, // 2022-05-01 17:34:58
    @SerializedName("people")
    val people: Int, // 8
    @SerializedName("table")
    val table: String, // 1
)