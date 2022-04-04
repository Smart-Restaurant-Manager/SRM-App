package com.srm.srmapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.srm.srmapp.R
import com.srm.srmapp.Utils.launchException
import com.srm.srmapp.data.dto.auth.response.toUser
import com.srm.srmapp.data.models.User
import com.srm.srmapp.repository.authentication.AuthInterface
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class UserSession @Inject constructor(context: Context, private val authInterface: AuthInterface) {
    private val prefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private val prefKey = context.getString(R.string.pref_key_token)
    private var userObject: MutableLiveData<User?> = MutableLiveData(null)
    private val scope = CoroutineName(javaClass.simpleName)

    init {
        refresUser()
    }

    fun setToken(token: String) {
        prefs.edit().apply {
            putString(prefKey, token)
            apply()
        }
    }

    fun isLoggedIn() = (prefs.getString(prefKey, "") ?: "").isNotBlank()

    fun getBearerToken() = "Bearer ${prefs.getString(prefKey, "")}"

    fun logout() {
        if (isLoggedIn()) {
            CoroutineScope(scope).launchException(Dispatchers.IO) {
                authInterface.logout(getBearerToken())
            }.invokeOnCompletion {
                userObject.postValue(null)
                setToken("")
            }
        }
    }

    fun refresUser() {
        if (isLoggedIn()) {
            CoroutineScope(scope).launchException(Dispatchers.IO) {
                val res = authInterface.getUser(getBearerToken())
                if (res.isSuccessful) {
                    userObject.postValue(res.body()?.toUser())
                } else {
                    logout()
                }
            }
        }
    }

    fun getUser() = userObject as LiveData<User?>
}