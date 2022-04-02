package com.srm.srmapp.repository.authentication

import com.srm.srmapp.Resource
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.data.dto.auth.LoginObject
import com.srm.srmapp.data.dto.auth.SignupObject
import com.srm.srmapp.repository.BaseRepository
import timber.log.Timber
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: AuthInterface, private val userSession: UserSession) : BaseRepository() {
    suspend fun login(email: String, password: String): Resource<String> {
        return safeApiCall({
            val loginObject = LoginObject(email = email, password = password, device_name = "API")
            api.login(loginObject)
        }) { response ->
            val token = response.data.token
            Timber.d(token, "token ")
            userSession.setToken(token)
            userSession.refresUser()
            "Logged in"
        }
    }

    suspend fun logout(): Resource<String> {
        return safeApiCall({
            api.logout(userSession.getBearerToken())
        }) { response ->
            userSession.logout()
            response.toString()
        }
    }

    suspend fun signup(email: String, name: String, password: String, passwordConfirmation: String): Resource<String> {
        return safeApiCall({
            val singupObject = SignupObject(email, name, password, passwordConfirmation)
            api.signup(singupObject)
        }) { response ->
            "User created with id ${response.data.id}"
        }
    }
}
