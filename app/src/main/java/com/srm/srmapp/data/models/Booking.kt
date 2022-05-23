package com.srm.srmapp.data.models

import com.srm.srmapp.data.dto.bookings.body.BookingObject
import java.time.LocalDateTime

data class Booking(
    val bookingId: Int = -1,
    val name: String,
    val email: String,
    val phone: String,
    val date: LocalDateTime,
    val people: Int,
    val table: String,
) : GetId {
    fun toJsonObject(): BookingObject {
        return BookingObject(name = name, email = email, phone = phone, date = date, people = people, table = table)
    }

    override fun getId(): Int {
        return bookingId
    }
}