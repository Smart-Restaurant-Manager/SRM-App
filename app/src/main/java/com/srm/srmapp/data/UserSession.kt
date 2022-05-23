package com.srm.srmapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.User
import timber.log.Timber
import javax.inject.Inject


class UserSession @Inject constructor(context: Context) {
    private val prefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private var token: String = ""
    private var _user: MutableLiveData<Resource<User>> = MutableLiveData(Resource.Empty())
    val user: LiveData<Resource<User>>
        get() = _user

    fun getBearerToken(): String {
        return if (token.isBlank())
            ""
        else
            "Bearer $token"
    }

    fun getRole() = user.value?.data?.role ?: -1

    fun setToken(token: String) {
        Timber.d("set Token $token")
        this.token = token
    }

    fun setUser(user: User) {
        Timber.d("Got user ${user.userId} ${user.email}")
        _user.postValue(Resource.Success(user))
    }

    fun setUsernameAndPassword(username: String, password: String) {
        prefs.edit().apply {
            putString("username", username)
            putString("password", password)
            apply()
        }
    }

    fun clearUsernameAndPassowrd() {
        setUsernameAndPassword("", "")
    }

    fun getUser() = prefs.getString("username", "")!!
    fun getPassword() = prefs.getString("password", "")!!

    fun isSaved() =
        prefs.getString("username", "")?.isNotBlank() ?: false &&
                prefs.getString("password", "")?.isNotBlank() ?: false

    fun logout() {
        token = ""
        _user.postValue(Resource.Empty())
    }
}