package com.srm.srmapp.ui.bookings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Booking
import com.srm.srmapp.repository.bookings.BookingRepository
import com.srm.srmapp.repository.orders.OrdersRepository
import com.srm.srmapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val ordersRepository: OrdersRepository,
) :
    BaseViewModel() {

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
        fetchResource(_status) {
            bookingRepository.putBooking(booking.bookingId, booking)
        }
    }

}