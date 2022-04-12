package com.srm.srmapp

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()

    fun isSuccess(): Boolean = this is Success
    fun isSuccessAndDataNotNull(): Boolean = this is Success && this.data != null
    fun isError(): Boolean = this is Error
}