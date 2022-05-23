package com.srm.srmapp.ui.bookings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.srm.srmapp.AppModule
import com.srm.srmapp.Resource
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.data.models.Booking
import com.srm.srmapp.repository.bookings.BookingRepository
import com.srm.srmapp.repository.orders.OrdersRepository
import com.srm.srmapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val ordersRepository: OrdersRepository,
    userSession: UserSession,
) :
    BaseViewModel(userSession) {

    val predicate: (Booking, String) -> Boolean = { booking, query ->
        val time = try {
            val timequery = LocalTime.from(AppModule.timeFormatter.parse(query))
            val time = booking.date.toLocalTime()
            time?.isAfter(timequery)
        } catch (e: DateTimeParseException) {
            Timber.d("Not a time query")
            null
        } ?: false

        val date = try {
            val dateQuery = LocalDate.from(AppModule.dateFormatter.parse(query))
            val date = booking.date.toLocalDate()
            date?.isAfter(dateQuery)
        } catch (e: DateTimeParseException) {
            Timber.d("Not a date query")
            null
        } ?: false

        val dateTime = try {
            val dateTimeQuery = LocalDateTime.from(AppModule.dateTimeFormatter.parse(query))
            val dateTime = booking.date
            dateTime.isAfter(dateTimeQuery)
        } catch (e: DateTimeParseException) {
            Timber.d("Not a date time query")
            null
        } ?: false


        val email = booking.email.startsWith(query, ignoreCase = false) ?: false
        val id = booking.toString().startsWith(query, ignoreCase = true)
        val table = booking.table.startsWith(query, ignoreCase = false) ?: false
        val name = booking.name.startsWith(query, ignoreCase = false) ?: false
        val isAfter = dateTime || date || time

        isAfter || id || name || email || table
    }

    //Refresh list
    private val _bookingList: MutableLiveData<Resource<List<Booking>>> = MutableLiveData()
    val bookingList: LiveData<Resource<List<Booking>>>
        get() = _bookingList

    //get booking by ID
    private val _book: MutableLiveData<Resource<Booking>> = MutableLiveData()
    val book: LiveData<Resource<Booking>>
        get() = _book


    fun addBooking(booking: Booking) {
        fetchResource(_status, onSuccess = {
            refreshBookingsList()
        }) {
            bookingRepository.postBooking(booking)
        }
    }

    fun refreshBookingsList() {
        fetchResource(this._bookingList) {
            bookingRepository.getBookings()
        }
    }

    fun getBookingBy(id: Int) {
        fetchResource(this._book) {
            bookingRepository.getBooking(id)
        }
    }

    fun deleteBooking(id: Int) {
        fetchResource(_status, onSuccess = {
            _bookingList.value?.data?.toMutableList()?.let { list ->
                list.removeIf { it.bookingId == id }
                _bookingList.postValue(Resource.Success(list.toList()))
            }
        }) {
            bookingRepository.deleteBooking(id)
        }
    }

    fun putBooking(booking: Booking) {
        fetchResource(_status, onSuccess = {
            refreshBookingsList()
        }) {
            bookingRepository.putBooking(booking.bookingId, booking)
        }
    }

}