package com.srm.srmapp.data.dto.bookings.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BookingListResponse(
    @SerializedName("data")
    val data: List<BookingResponse.Data>,
)

fun BookingListResponse.toBookingList() = data.map { it.toBooking() }