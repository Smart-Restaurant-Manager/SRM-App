package com.srm.srmapp.ui.bookings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.srm.srmapp.Resource
import com.srm.srmapp.Utils.fetchResource
import com.srm.srmapp.data.models.Booking
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.repository.bookings.BookingRepository
import com.srm.srmapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(val bookingRepository: BookingRepository) : BaseViewModel() {
    //Refresh list
    private val _bookingList: MutableLiveData<Resource<List<Booking>>> = MutableLiveData()
    val bookingList: LiveData<Resource<List<Booking>>>
        get() = _bookingList

    //get booking by ID
    private val _book: MutableLiveData<Resource<Booking>> = MutableLiveData()
    val book: LiveData<Resource<Booking>>
        get() = _book


    fun addBooking(name: String, amountPeople: Int, date: String, telephone: String, mail: String, table: String) {
        fetchResource(this._status) {
            bookingRepository.postBooking(Booking(id = null, name, mail, telephone, LocalDateTime.now(), amountPeople, table))
        }
    }

    fun refreshBookingsList(){
        fetchResource(this._bookingList){
            bookingRepository.getBookings()
        }
    }
    fun getBookingBy(id: Int){
        fetchResource(this._book){
            bookingRepository.getBooking(id)
        }
    }

    fun deleteBooking(id: Int){
        fetchResource(_status, onSuccess = {
            _bookingList.value?.data?.toMutableList()?.let{ list ->
                list.removeIf {it.id == id}
                _bookingList.postValue(Resource.Success(list.toList()))
            }
        }){
            bookingRepository.deleteBooking(id)

        }
    }

    fun putBooking(id:Int, book: Booking){
        fetchResource(_status){
            bookingRepository.putBooking(id, book)
        }
    }

}