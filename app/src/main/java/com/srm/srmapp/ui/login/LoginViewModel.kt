package com.srm.srmapp.ui.login

import com.srm.srmapp.data.UserSession
import com.srm.srmapp.repository.authentication.AuthRepository
import com.srm.srmapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: AuthRepository, userSession: UserSession) : BaseViewModel(userSession) {

    fun login(username: String, password: String) {
        Timber.d("login $username $password")
        fetchResource(_status) {
            repository.login(username, password)
            val res = repository.getUser()
            Timber.d("user resource $res")
            res
        }
    }

    fun getUser() {
        fetchResource(_status) {
            repository.getUser()
        }
    }

    fun signup(email: String, name: String, password: String, passwordCheck: String) {
        Timber.d("signup $email $name $password $passwordCheck")
        fetchResource(_status) {
            val res = repository.signup(email, name, password, passwordCheck)
            Timber.d("${res.data}")
            res
        }
    }
}