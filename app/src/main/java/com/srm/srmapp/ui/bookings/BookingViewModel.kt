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
    private val _recipeList: MutableLiveData<Resource<List<Recipe>>> = MutableLiveData()
    val recipeList: LiveData<Resource<List<Recipe>>>
        get() = _recipeList

    fun addBooking(name: String, amountPeople: Int, date: String, telephone: String, mail: String, table: String) {
        fetchResource(this._status) {
            bookingRepository.postBooking(Booking(id = null, name, mail, telephone, LocalDateTime.now(), amountPeople, table))
        }
    }
}