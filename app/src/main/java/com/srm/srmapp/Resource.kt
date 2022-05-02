package com.srm.srmapp

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
    class Empty<T> : Resource<T>()

    fun isSuccess(): Boolean = this is Success
    fun isEmpty(): Boolean = this is Empty
    fun isLoading(): Boolean = this is Loading
    fun isError(): Boolean = this is Error
    override fun toString(): String {
        return when (this) {
            is Success -> "Success $data"
            is Error -> "Error $data $message"
            is Loading -> "Loading"
            is Empty -> "Empty"
        }
    }
}