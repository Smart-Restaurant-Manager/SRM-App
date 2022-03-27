package com.srm.srmapp.repository

import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.User
import timber.log.Timber
import javax.inject.Inject

class Repository @Inject constructor(private val api: ApiInterface) {

    suspend fun login(username: String, password: String): Resource<User> {
        Timber.i("Repository login")
        return try {
            val res = api.login(username, password)
            val data = res.body()
            if (res.isSuccessful && data != null) {
                Timber.i("Login success")
                Resource.Success(data = User(username = username, token = data.token, tokenExpired = false))
            } else { // TODO handle non 200 responses
                Timber.w("Error on login")
                Resource.Error("Error on login")
            }
        } catch (e: Exception) {
            Timber.i("Login exception ${e.localizedMessage}")
            Resource.Error("Exception on login ${e.localizedMessage}")
        }
    }
}
