package com.srm.srmapp.repository

import com.srm.srmapp.Resource
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response

abstract class BaseRepository {
    suspend fun <R, T> safeApiCall(apiCall: suspend () -> Response<R>, responseConverter: (R) -> T): Resource<T> {
        return try {
            val res = apiCall.invoke()
            val data = res.body()
            if (res.isSuccessful && data != null)
                Resource.Success(data = responseConverter.invoke(data))
            else
                throw HttpException(res)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Resource.Error("Not found")
                // TODO handle other codes
                else -> Resource.Error("Http error ${e.code()} ${e.response()?.errorBody()?.string()}")
            }
        } catch (e: IOException) {
            Resource.Error("No internet")
        }
    }
}