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
            Timber.w("Handle http exception ${e.code()}")
            when (e.code()) {
                404 -> Resource.Error("No encontrado", e.code())
                401 -> Resource.Error("No autorizado", e.code())
                422 -> Resource.Error("Error en datos: \n${e.response()?.errorBody()?.string()}", e.code())
                500 -> Resource.Error("Error de servidor, intentar mas tarde.", e.code())
                409 -> Resource.Error("No se puede eliminar ya que tiene stocks, recetas o comandas relacionadas\n${e.response()?.errorBody()?.string()}")
                else -> Resource.Error("Http error ${e.code()} ${e.response()?.errorBody()?.string()}", e.code())
            }
        } catch (e: MalformedJsonException) {
            Timber.w(e)
            Resource.Error("No JSON content")
        } catch (e: IOException) {
            Timber.w(e)
            Resource.Error("No internet")
        }
    }
}