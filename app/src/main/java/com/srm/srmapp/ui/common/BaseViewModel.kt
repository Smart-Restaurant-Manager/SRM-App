package com.srm.srmapp.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srm.srmapp.Resource
import com.srm.srmapp.Utils.launchException

abstract class BaseViewModel : ViewModel() {
    protected val _status: MutableLiveData<Resource<String>> = MutableLiveData()
    val status: LiveData<Resource<String>>
        get() {
            val t = _status
            _status.postValue(Resource.Empty())
            return t
        }

    fun <T> fetchResource(
        livedataResource: MutableLiveData<Resource<T>>,
        onSuccess: suspend (Resource<T>) -> Unit = {},
        repositoryCall: suspend () -> Resource<T>,
    ) {
        livedataResource.postValue(Resource.Loading())
        this.viewModelScope.launchException {
            val r = repositoryCall.invoke()
            if (r.isSuccess())
                onSuccess.invoke(r)
            livedataResource.postValue(r)
        }
    }
}