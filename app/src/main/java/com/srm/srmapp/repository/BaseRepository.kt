package com.srm.srmapp.repository

import com.srm.srmapp.Resource
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import java.time.LocalTime
import kotlin.reflect.jvm.ExperimentalReflectionOnLambdas

@OptIn(ExperimentalReflectionOnLambdas::class)
abstract class BaseRepository {
    data class CacheWrapper(val data: Any, val expireAfterSeconds: Long = 15) {
        private val localTime = LocalTime.now()
        fun isOutOfDate() = LocalTime.now().isAfter(this.localTime.plusSeconds(expireAfterSeconds))
    }

    private val cache: Map<Any, CacheWrapper> = emptyMap()


    suspend fun <R, T> safeApiCall(apiCall: suspend () -> Response<R>, responseConverter: (R) -> T): Resource<T> {
        return try {
            val res = apiCall.invoke()
            val data = res.body()
            @Suppress("UNCHECKED_CAST")
            if (res.isSuccessful)
                Resource.Success(data = responseConverter.invoke(data ?: Unit as R))
            else
                throw HttpException(res)
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> Resource.Error("Not found")
                401 -> Resource.Error("Unauthorized")
                500 -> Resource.Error("Server Error, try later")
                else -> Resource.Error("Http error ${e.code()} ${e.response()?.errorBody()}")
            }
        } catch (e: IOException) {
            Resource.Error("No internet")
        }
    }
}