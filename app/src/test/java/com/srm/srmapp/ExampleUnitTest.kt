package com.srm.srmapp

import com.google.gson.Gson
import com.srm.srmapp.data.dto.auth.body.LoginObject
import com.srm.srmapp.data.dto.stock.body.FoodObject
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Stock
import com.srm.srmapp.repository.authentication.AuthInterface
import com.srm.srmapp.repository.orders.OrdersInterface
import com.srm.srmapp.repository.stock.StockInterface
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


class ExampleUnitTest {
    private val BASE_URL = "https://smart-restaurant-manager.herokuapp.com"

    val gson: Gson = AppModule.provideGsonConverter()
    private val retrofit2 = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(OkHttpClient.Builder()
            .followRedirects(false)
            .followSslRedirects(false)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build())
        .build()

    private var token: String = ""
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(OkHttpClient.Builder()
            .followRedirects(false)
            .followSslRedirects(false)
            .addInterceptor {
                val req = it.request()
                it.proceed(req
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build())
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build())
        .build()

    private val authapi = retrofit2.create(AuthInterface::class.java)

    init {
        token = runBlocking {
            authapi.login(LoginObject("123@123", "123", "123"))
        }.body()?.data?.token ?: ""
    }

    private val stockapi = retrofit.create(StockInterface::class.java)
    private val stocklist = listOf(
        Stock(-1, -1, 0f, LocalDate.now()),
        Stock(-1, -1, 1f, LocalDate.now()),
    )
    private val foodList: List<Food> = listOf(
        Food("", -1, "testFood1112", "kg"),
        Food("", -1, "testFood32213", "l"),
    )

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
        foodList.forEach {
            assert(runBlocking {
                stockapi.postFood(it.toJsonObject())
            }.isSuccessful)
        }

        // get food list
        val response = runBlocking {
            stockapi.getFood()
        }
        assert(response.isSuccessful)

        val foodlistRes = response.body()?.toFoodList()
        assert(foodlistRes != null)

        // put food
        foodlistRes?.forEach {
            assert(
                runBlocking {
                    stockapi.putFood(it.foodId, FoodObject(it.name.reversed(), it.units.reversed(), ""))
                }.isSuccessful
            )
        }

        // get food list
        val foodlistReversed = runBlocking {
            stockapi.getFood()
        }.body()?.toFoodList()
        assert(foodlistReversed != null)

        // check if we modified correctly by counting
        var count = 0
        foodlistReversed?.forEach { res ->
            if (foodList.find { it.name == res.name.reversed() && it.units == res.units.reversed() } != null) {
                count++
            }
        }
        assert(count == foodList.size)


        // delete food
        foodlistRes?.forEach { res ->
            if (foodList.find { it.name == res.name } != null)
                assert(runBlocking {
                    stockapi.deleteFood(res.foodId)
                }.isSuccessful)
        }
    }


    private val orderApi = retrofit.create(OrdersInterface::class.java)

    @Test
    fun testOrder() {
        val ordersRes = runBlocking {
            orderApi.getOrders()
        }
        assert(ordersRes.isSuccessful)
    }
}