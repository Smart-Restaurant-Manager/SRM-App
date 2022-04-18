package com.srm.srmapp

import com.google.gson.GsonBuilder
import com.srm.srmapp.data.dto.auth.body.LoginObject
import com.srm.srmapp.data.dto.stock.body.FoodObject
import com.srm.srmapp.repository.authentication.AuthInterface
import com.srm.srmapp.repository.stock.StockInterface
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val BASE_URL = "https://smart-restaurant-manager.herokuapp.com"
    private val retrofit2 = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
            .setDateFormat("dd-MM-yyyy'T'HH:mm:ssz")
            .create()))
        .client(OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
            .setDateFormat("dd-MM-yyyy'T'HH:mm:ssz")
            .create()))
        .client(OkHttpClient.Builder()
            .addInterceptor {
                val req = it.request()
                it.proceed(req
                    .newBuilder()
                    .addHeader("Authorization", "Bearer 91|J21GEULgzmj0LLOv3lsmWk7C0HJRyYwFx6QLLUWO")
                    .build())

            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build())
        .build()

    private val authapi = retrofit2.create(AuthInterface::class.java)
    private val stockapi = retrofit.create(StockInterface::class.java)


    @Test
    fun login_logout() {
        val response = runBlocking {
            authapi.login(LoginObject("123@123", "123", "123"))
        }
        val data = response.body()?.data?.token
        assert(data != null)
        val response2 = runBlocking {
            authapi.logout("Bearer $data")
        }
        assert(response2.isSuccessful)
    }

    @Test
    fun testStockApi() {
        // insert food
        assert(runBlocking {
            stockapi.postFood(FoodObject("test", "test"))
        }.isSuccessful)

        // get food list
        val response = runBlocking {
            stockapi.getFood()
        }
        assert(response.isSuccessful)

        val data = response.body()
        assert(data != null)

        val foodlist = data?.toFoodList()!!

        // find food
        val food = foodlist.find {
            it.name == "test" && it.units == "test"
        }
        assert(food != null)

        // delete food
        assert(runBlocking {
            stockapi.deleteFood(food!!.id)
        }.isSuccessful)
    }
}