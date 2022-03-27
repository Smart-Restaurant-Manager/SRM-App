package com.srm.srmapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.User
import com.srm.srmapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _loginState: MutableLiveData<Resource<User>> = MutableLiveData()
    fun getLoginState() = _loginState as LiveData<Resource<User>>

    fun login(username: String, password: String) {
        Timber.d("Viewmodel login")
        _loginState.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            Timber.d("Viewmodel login coroutine")
            _loginState.postValue(repository.login(username, password))
        }
    }

}