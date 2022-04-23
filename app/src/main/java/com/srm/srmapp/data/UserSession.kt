package com.srm.srmapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.Utils.launchException
import com.srm.srmapp.data.dto.auth.response.toUser
import com.srm.srmapp.data.models.User
import com.srm.srmapp.repository.authentication.AuthInterface
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject


class UserSession @Inject constructor(context: Context, private val authInterface: AuthInterface) {
    private val prefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private val prefKey = context.getString(R.string.pref_key_token)
    private var _userObject: MutableLiveData<Resource<User>> = MutableLiveData(Resource.Empty())
    private var _loggedIn: MutableLiveData<Boolean> = MutableLiveData(false)
    private val scope = CoroutineName(javaClass.simpleName)
    val userObject: LiveData<Resource<User>>
        get() = _userObject
    val loggedIn: LiveData<Boolean>
        get() = _loggedIn

    fun setToken(token: String) {
        prefs.edit().apply {
            putString(prefKey, token)
            apply()
        }
    }

    fun isLoggedIn() = (prefs.getString(prefKey, "") ?: "").isNotBlank()

    fun getBearerToken() = "Bearer ${prefs.getString(prefKey, "")}"

    fun logout() {
        _userObject.postValue(Resource.Loading())
        CoroutineScope(scope).launchException {
            authInterface.logout(getBearerToken())
        }.invokeOnCompletion {
            _userObject.postValue(Resource.Empty())
            _loggedIn.postValue(false)
            setToken("")
        }
    }

    fun refresUser() {
        _userObject.postValue(Resource.Loading())
        CoroutineScope(scope).launchException {
            val res = authInterface.getUser(getBearerToken())
            val resBody = res.body()
            if (res.isSuccessful && resBody != null) {
                Timber.d("Got user ${res.body()?.email}")
                _userObject.postValue(Resource.Success(resBody.toUser()))
                _loggedIn.postValue(true)
            } else {
                Timber.d("No user found")
                logout()
                _userObject.postValue(Resource.Error("No user found"))
                _loggedIn.postValue(false)
            }
        }
    }
}