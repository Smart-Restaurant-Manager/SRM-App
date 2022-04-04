package com.srm.srmapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srm.srmapp.Resource
import com.srm.srmapp.Utils.launchException
import com.srm.srmapp.repository.authentication.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _loginState: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getLoginState() = _loginState as LiveData<Resource<String>>

    private val _signupState: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getSignupState() = _signupState as LiveData<Resource<String>>

    private var formToggle = true
    fun getFormtoggle() = formToggle
    fun toggleForm() {
        formToggle = !formToggle
    }

    fun login(username: String, password: String) {
        Timber.d("login $username $password")
        _loginState.value = Resource.Loading()
        viewModelScope.launchException(Dispatchers.IO) {
            val res = repository.login(username, password)
            _loginState.postValue(res)
        }
    }


    fun signup(email: String, name: String, password: String, passwordCheck: String) {
        Timber.d("signup $email $name $password $passwordCheck")
        _signupState.value = Resource.Loading()
        viewModelScope.launchException(Dispatchers.IO) {
            val res = repository.signup(email, name, password, passwordCheck)
            _signupState.postValue(res)
            Timber.d("${res.data}")
        }
    }
}