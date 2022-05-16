package com.srm.srmapp

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val errorCode: Int? = null,
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, errorCode: Int? = null, data: T? = null) : Resource<T>(data, message, errorCode)
    class Loading<T> : Resource<T>()
    class Empty<T> : Resource<T>()

    fun isSuccess(): Boolean = this is Success
    fun isEmpty(): Boolean = this is Empty
    fun isLoading(): Boolean = this is Loading
    fun isError(): Boolean = this is Error
    override fun toString(): String {
        return when (this) {
            is Success -> "Success $data"
            is Error -> "Error $data $message $errorCode"
            is Loading -> "Loading"
            is Empty -> "Empty"
        }
    }

    fun <T> wrapThis(other: T) = when (this) {
        is Empty -> Empty()
        is Error -> Error("Error")
        is Loading -> Loading()
        is Success -> Success(data = other)
    }
}