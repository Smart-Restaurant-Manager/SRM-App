package com.srm.srmapp.repository.bookings

import com.srm.srmapp.data.dto.bookings.body.BookingObject
import com.srm.srmapp.data.dto.bookings.response.BookingListResponse
import com.srm.srmapp.data.dto.bookings.response.BookingResponse
import retrofit2.Response
import retrofit2.http.*

interface BookingInterface {
    @GET("https://smart-restaurant-manager.herokuapp.com/api/v1/bookings")
    suspend fun getBookings(): Response<BookingListResponse>

    @POST("https://smart-restaurant-manager.herokuapp.com/api/v1/bookings")
    suspend fun postBookings(@Body bookingObject: BookingObject): Response<Unit>

    @GET("https://smart-restaurant-manager.herokuapp.com/api/v1/bookings/{id}")
    suspend fun getBooking(@Path("id") id: Int): Response<BookingResponse>

    @DELETE("https://smart-restaurant-manager.herokuapp.com/api/v1/bookings/{id}")
    suspend fun deleteBooking(@Path("id") id: Int): Response<BookingResponse>

    @PUT("https://smart-restaurant-manager.herokuapp.com/api/v1/bookings/{id}")
    suspend fun putBooking(@Path("id") id: Int, @Body bookingObject: BookingObject): Response<Unit>
}