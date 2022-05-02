package com.srm.srmapp

import com.google.gson.Gson
import com.srm.srmapp.data.dto.auth.body.LoginObject
import com.srm.srmapp.data.dto.bookings.body.BookingObject
import com.srm.srmapp.data.dto.bookings.response.toBooking
import com.srm.srmapp.data.dto.bookings.response.toBookingList
import com.srm.srmapp.data.dto.orders.response.toOrderList
import com.srm.srmapp.data.dto.recipe.response.toRecipe
import com.srm.srmapp.data.dto.recipe.response.toRecipeList
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Order
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.data.models.Stock
import com.srm.srmapp.repository.authentication.AuthInterface
import com.srm.srmapp.repository.bookings.BookingInterface
import com.srm.srmapp.repository.orders.OrdersInterface
import com.srm.srmapp.repository.recipes.RecipeInterface
import com.srm.srmapp.repository.stock.StockInterface
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime

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
        val food = Food("test", -1, "testFood32213", "l")


        // insert food
        assert(runBlocking {
            stockapi.postFood(food.toJsonObject())
        }.isSuccessful)

        // get food list
        val response = runBlocking {
            stockapi.getFood()
        }
        assert(response.isSuccessful)

        val foodlistRes = response.body()?.toFoodList()

        assert(foodlistRes != null)
        foodlistRes!!
        val lastId = foodlistRes.last().foodId

        // put food
        assert(runBlocking {
            stockapi.putFood(lastId, food.toJsonObject())
        }.isSuccessful)

        val stock = Stock(-1, lastId, 1f, LocalDate.now()).toJsonObject()
        // insert stock
        assert(runBlocking {
            stockapi.postStock(stockObject = stock)
        }.isSuccessful)

        // get stock
        val stockRes = runBlocking {
            stockapi.getFoodStock(lastId)
        }

        assert(stockRes.isSuccessful)
        val a = stockRes.body()?.toStockList()
        assert(a!!.size == 1)

        // put stock
        val lastStockId = a.last().stockId
        assert(runBlocking { stockapi.putStock(lastStockId, stock).isSuccessful })


        // delete stock
        assert(runBlocking { stockapi.deleteStock(lastStockId).isSuccessful })


        // delete food
        assert(runBlocking { stockapi.deleteFood(lastId).isSuccessful })
    }

    private val recipeApi = retrofit.create(RecipeInterface::class.java)

    @Test
    fun testRecipe() {
        val recipeModel = Recipe(name = "a", type = Recipe.RecipeType.NONE, id = 1, price = 1f)
        assert(runBlocking {
            recipeApi.postRecipe(recipeModel.toJsonObject())
        }.isSuccessful)

        val r = runBlocking {
            recipeApi.getRecipes()
        }

        assert(r.isSuccessful)
        val o = r.body()?.toRecipeList()

        assert(o != null)
        o!!
        val lastId = o.last().id

        val recipeRes = runBlocking {
            recipeApi.getRecipe(lastId)
        }

        assert(recipeRes.isSuccessful)

        val recipe = recipeRes.body()?.toRecipe()

        assert(recipe != null)
        recipe!!

        assert(runBlocking { recipeApi.putRecipe(recipe.id, recipeModel.toJsonObject()).isSuccessful })

        assert(runBlocking { recipeApi.deleteRecipe(recipe.id).isSuccessful })
    }


    private val orderApi = retrofit.create(OrdersInterface::class.java)

    private fun createBooking(): Int {
        val bookingObject = BookingObject(name = "a", email = "a@a", phone = "1", date = LocalDateTime.now(), people = 1, table = "1")

        val bookingGetRes = runBlocking {
            assert(bookingApi.postBookings(bookingObject = bookingObject).isSuccessful)
            val res = bookingApi.getBookings()
            assert(res.isSuccessful)
            res
        }

        val bookingList = bookingGetRes.body()?.toBookingList()
        val lastId = bookingList?.last()?.id

        return lastId!!
    }

    private fun createRecipe(): Int {
        val recipeModel = Recipe(name = "a", type = Recipe.RecipeType.NONE, id = 1, price = 1f)
        val r = runBlocking {
            assert(recipeApi.postRecipe(recipeModel.toJsonObject()).isSuccessful)
            val res = recipeApi.getRecipes()
            assert(res.isSuccessful)
            res
        }
        val o = r.body()?.toRecipeList()
        return o!!.last().id
    }

    @Test
    fun testOrder() {
        val id = createBooking()
        val recipeId = createRecipe()
        val orderJson =
            Order(bookingId = id, recipeList = listOf(Order.OrderRecipe(recipeId = recipeId, quantity = 1, price = 1.0, type = 0))).toJsonObject()

        assert(runBlocking { orderApi.postOrder(orderJson).isSuccessful })

        val ordersRes = runBlocking {
            orderApi.getOrders()
        }
        assert(ordersRes.isSuccessful)

        val lastOrderId = ordersRes.body()?.toOrderList()?.last()?.orderId
        assert(lastOrderId != null)
        lastOrderId!!

        assert(runBlocking { orderApi.getOrder(lastOrderId).isSuccessful })

        assert(runBlocking { orderApi.putOrder(lastOrderId, orderJson).isSuccessful })

        assert(runBlocking { orderApi.deleteOrder(lastOrderId).isSuccessful })
    }


    private val bookingApi = retrofit.create(BookingInterface::class.java)

    @Test
    fun testBooking() {
        val bookingObject = BookingObject(name = "a", email = "a@a", phone = "1", date = LocalDateTime.now(), people = 1, table = "1")
        val bookingPostRes = runBlocking {
            bookingApi.postBookings(bookingObject = bookingObject)
        }
        assert(bookingPostRes.isSuccessful)

        val bookingGetRes = runBlocking {
            bookingApi.getBookings()
        }
        assert(bookingGetRes.isSuccessful)

        val bookingList = bookingGetRes.body()?.toBookingList()

        assert(bookingList != null)

        val lastId = bookingList?.last()?.id

        assert(lastId != null)
        lastId!!

        val bookingRes = runBlocking {
            bookingApi.getBooking(lastId)
        }
        assert(bookingRes.isSuccessful)

        val bo = bookingRes.body()?.toBooking()

        assert(bo != null)

        bo!!

        assert(bo.id == lastId)

        assert(runBlocking { bookingApi.putBooking(lastId, bookingObject).isSuccessful })

        assert(runBlocking { bookingApi.deleteBooking(lastId) }.isSuccessful)
    }

}