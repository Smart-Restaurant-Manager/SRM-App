package com.srm.srmapp.ui.bookings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.srm.srmapp.Resource
import com.srm.srmapp.Utils.fetchResource
import com.srm.srmapp.data.models.Booking
import com.srm.srmapp.repository.bookings.BookingRepository
import com.srm.srmapp.repository.orders.OrdersRepository
import com.srm.srmapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject

data class BookingDataHolder(
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var date: String = "",
    var people: String = "",
    var table: String = "",
) {
    fun toBooking() = Booking(-1, name, email, phone, LocalDateTime.now(), people.toInt(), table)

    companion object {
        fun fromBooking(b: Booking): BookingDataHolder = BookingDataHolder(b.name, b.email, b.phone, b.email, b.people.toString(), b.table)
    }
}

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


    fun addBooking(bookingDataHolder: BookingDataHolder) {
        fetchResource(this._status, onSuccess = {
            refreshBookingsList()
        }) {
            bookingRepository.postBooking(bookingDataHolder.toBooking())
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
                list.removeIf { it.id == id }
                _bookingList.postValue(Resource.Success(list.toList()))
            }
        }) {
            bookingRepository.deleteBooking(id)
        }
    }

    fun putBooking(id: Int, bookingUIState: BookingDataHolder) {
        fetchResource(_status) {
            bookingRepository.putBooking(id, bookingUIState.toBooking())
        }
    }

}