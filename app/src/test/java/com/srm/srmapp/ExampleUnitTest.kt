package com.srm.srmapp

import com.google.gson.GsonBuilder
import com.srm.srmapp.data.dto.auth.body.LoginObject
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Stock
import com.srm.srmapp.repository.authentication.AuthInterface
import com.srm.srmapp.repository.stock.StockInterface
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

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

    private var token: String = ""
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
        Stock(-1, -1, 0f, Calendar.getInstance().time),
        Stock(-1, -1, 1f, Calendar.getInstance().time),
        Stock(-1, -1, 2f, Calendar.getInstance().time),
        Stock(-1, -1, 3f, Calendar.getInstance().time),
        Stock(-1, -1, 4f, Calendar.getInstance().time),
        Stock(-1, -1, 5f, Calendar.getInstance().time),
    )
    private val foodList: List<Food> = listOf(
        Food("Carne", -1, "Carne 1", "kg"),
        Food("Carne", -1, "Leche 1", "l"),
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
        assert(runBlocking {
            stockapi.postFood(foodList[0].toJsonObject())
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
            it.name == foodlist[0].name && it.units == foodlist[0].units
        }

        assert(food != null)
        food!!
        food.addStock(stocklist[0])
        food.addStock(stocklist[1])

        // add stock
        for (s in food.stockList) {
            assert(
                runBlocking {
                    stockapi.postStock(s.toJsonObject())
                }.isSuccessful
            )
        }

        // get stock for food
        val stockres = runBlocking {
            stockapi.getFoodStock(food.foodId)
        }

        assert(stockres.isSuccessful)

        val stocks = stockres.body()?.toStockList()
        assert(stocks != null)
        stocks!!

        // validate stocks
        for ((s1, s2) in stocks.zip(food.stockList)) {
            assert(s1 == s2)
        }

        // delete stocks
        for (s in stocks) {
            assert(
                runBlocking {
                    stockapi.deleteStock(s.stockId)
                }.isSuccessful
            )
        }

        // delete food
        assert(runBlocking {
            stockapi.deleteFood(food.foodId)
        }.isSuccessful)
    }
}