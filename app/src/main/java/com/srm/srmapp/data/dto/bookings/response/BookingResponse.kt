package com.srm.srmapp.data.dto.bookings.response


import com.google.gson.annotations.SerializedName
import com.srm.srmapp.data.models.Booking
import java.time.LocalDateTime

data class BookingResponse(
    @SerializedName("data")
    val data: Data,
) {
    data class Data(
        @SerializedName("type")
        val type: String, // bookings
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("attributes")
        val attributes: Attributes,
    ) {
        data class Attributes(
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
            @SerializedName("created_at")
            val createdAt: LocalDateTime, // 2022-05-01T17:49:34.000000Z
            @SerializedName("updated_at")
            val updatedAt: LocalDateTime, // 2022-05-01T17:49:34.000000Z
        )
    }
}

fun BookingResponse.Data.toBooking() =
    Booking(name = attributes.name,
        id = id,
        email = attributes.email,
        phone = attributes.phone,
        date = attributes.date,
        people = attributes.people,
        table = attributes.table)