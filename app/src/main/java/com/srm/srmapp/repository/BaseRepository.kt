package com.srm.srmapp.repository

import com.google.gson.stream.MalformedJsonException
import com.srm.srmapp.Resource
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
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
            Timber.e(e)
            when (e.code()) {
                404 -> Resource.Error("Not found", e.code())
                401 -> Resource.Error("Unauthorized", e.code())
                500 -> Resource.Error("Server Error, try later", e.code())
                else -> Resource.Error("Http error ${e.code()} ${e.response()?.errorBody()}", e.code())
            }
        } catch (e: MalformedJsonException) {
            Timber.e(e)
            Resource.Error("No JSON content")
        } catch (e: IOException) {
            Timber.e(e)
            Resource.Error("No internet")
        }
    }
}