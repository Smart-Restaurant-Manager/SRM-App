package com.srm.srmapp.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.srm.srmapp.Resource

abstract class BaseViewModel : ViewModel() {
    protected val _status: MutableLiveData<Resource<String>> = MutableLiveData()
    val status: LiveData<Resource<String>>
        get() {
            val t = _status
            _status.postValue(Resource.Empty())
            return t
        }
}