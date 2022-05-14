package com.srm.srmapp.repository.bookings

import com.srm.srmapp.data.dto.bookings.response.toBooking
import com.srm.srmapp.data.dto.bookings.response.toBookingList
import com.srm.srmapp.data.models.Booking
import com.srm.srmapp.repository.BaseRepository
import javax.inject.Inject

class BookingRepository @Inject constructor(private val bookingInterface: BookingInterface) : BaseRepository() {

    suspend fun getBookings() = safeApiCall({
        bookingInterface.getBookings()
    }) {
        it.toBookingList()
    }


    suspend fun getBooking(id: Int) = safeApiCall({
        bookingInterface.getBooking(id)
    }) {
        it.toBooking()
    }

    suspend fun postBooking(booking: Booking) = safeApiCall({
        bookingInterface.postBookings(booking.toJsonObject())
    }) {
        "Reserva Añadida"
    }

    suspend fun putBooking(id: Int, booking: Booking) = safeApiCall({
        bookingInterface.putBooking(id, booking.toJsonObject())
    }) {
        "Reserva modificada"
    }

    suspend fun deleteBooking(id: Int) = safeApiCall({
        bookingInterface.deleteBooking(id)
    }) {
        "Reserva eliminada"
    }
}