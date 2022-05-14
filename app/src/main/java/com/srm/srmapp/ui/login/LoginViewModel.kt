package com.srm.srmapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.srm.srmapp.Resource
import com.srm.srmapp.repository.authentication.AuthRepository
import com.srm.srmapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: AuthRepository) : BaseViewModel() {

    private val _loginState: MutableLiveData<Resource<String>> = MutableLiveData()
    val loginState: LiveData<Resource<String>>
        get() = _loginState
    private val _signupState: MutableLiveData<Resource<String>> = MutableLiveData()
    val signupState: LiveData<Resource<String>>
        get() = _signupState

    fun clearLoginStatus() {
        _loginState.value = Resource.Empty()
    }

    fun login(username: String, password: String) {
        Timber.d("login $username $password")
        fetchResource(_loginState) {
            val res = repository.login(username, password)
            Timber.d("user resource $res")
            res
        }
    }


    fun signup(email: String, name: String, password: String, passwordCheck: String) {
        Timber.d("signup $email $name $password $passwordCheck")
        fetchResource(_signupState) {
            val res = repository.signup(email, name, password, passwordCheck)
            Timber.d("${res.data}")
            res
        }
    }
}