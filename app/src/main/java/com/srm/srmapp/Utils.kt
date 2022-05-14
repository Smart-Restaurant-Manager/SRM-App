package com.srm.srmapp

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

object Utils {
    fun requestRuntimePermissions(activity: Activity) {
        Timber.i("Requesting permissions")
        val allNeededPermissions = getRequiredPermissions(activity).filter {
            checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (allNeededPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity, allNeededPermissions.toTypedArray(), /* requestCode= */ 0)
        }
    }

    fun allPermissionsGranted(context: Context): Boolean =
        getRequiredPermissions(context).all { checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }

    private fun getRequiredPermissions(context: Context): Array<String> {
        return try {
            val info = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if (ps != null && ps.isNotEmpty()) ps else arrayOf()
        } catch (e: Exception) {
            arrayOf()
        }
    }


    val exceptionHandler = CoroutineExceptionHandler { context, ex ->
        Timber.e("${ex.message} \n${Log.getStackTraceString(ex)}")
    }

    val Dispatchers.CUSTOM_DISPATCHER: CoroutineContext
        get() = exceptionHandler + IO

    fun CoroutineScope.launchException(coroutineContext: CoroutineContext = Dispatchers.IO, call: suspend CoroutineScope.() -> Unit): Job {
        return this.launch(exceptionHandler + coroutineContext) {
            call()
        }
    }

    fun Float.format(digits: Int): String {
        val decimal = this - this.toInt()
        if (decimal == 0f) return "%.${0}f".format(this)
        return "%.${digits}f".format(this)
    }
}