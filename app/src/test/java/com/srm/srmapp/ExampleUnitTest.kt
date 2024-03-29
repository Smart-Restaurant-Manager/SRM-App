package com.srm.srmapp

import com.srm.srmapp.data.dto.auth.response.UserResponse
import com.srm.srmapp.data.dto.bookings.body.BookingObject
import com.srm.srmapp.data.dto.bookings.response.toBookingList
import com.srm.srmapp.data.dto.orders.response.toOrderList
import com.srm.srmapp.data.dto.recipe.response.toRecipeList
import com.srm.srmapp.data.dto.stock.response.toFoodList
import com.srm.srmapp.data.dto.stock.response.toStockList
import com.srm.srmapp.data.models.*
import com.srm.srmapp.repository.BaseRepository
import com.srm.srmapp.repository.bookings.BookingInterface
import com.srm.srmapp.repository.orders.OrdersInterface
import com.srm.srmapp.repository.orders.OrdersRepository
import com.srm.srmapp.repository.recipes.RecipeInterface
import com.srm.srmapp.repository.recipes.RecipeRepository
import com.srm.srmapp.repository.stock.StockInterface
import com.srm.srmapp.repository.stock.StockRepository
import kotlinx.coroutines.CoroutineScope
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.runBlocking as runBlocking2

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


class ExampleUnitTest {
    private fun <T> runBlocking(block: suspend CoroutineScope.() -> T): T {
        Thread.sleep(1000)
        val a = runBlocking2 {
            block()
        }
        if (a is Response<*> && !a.isSuccessful) {
            val e = HttpException(a)

            println("${e.code()} ${e.localizedMessage}")
        }
        return a
    }


    private val retrofit = Retrofit.Builder()
        .baseUrl(AppModule.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(AppModule.provideGsonConverter()))
        .client(
            OkHttpClient.Builder()
                .addInterceptor(Interceptor {
                    val re = it.request()
                        .newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                    re.addHeader("Authorization", "Bearer 13|8GYAzCaYhsBiIrhrKMegHJt2VmRkxWyLEk7uFrEZ")
                    val req = re.build()
                    it.proceed(req)
                })
                .addInterceptor(HttpLoggingInterceptor()
                    .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE))
                .followRedirects(false)
                .followSslRedirects(false)
                .build()
        )
        .build()

    private val stockapi = retrofit.create(StockInterface::class.java)

    @Test
    fun testStockApi() {
        val food = Food("Alimentos carnicos", -1, "test", "l")

        // insert food
        assert(runBlocking { stockapi.postFood(food.toJsonObject()) }.isSuccessful)

        // get food list
        val response = runBlocking { stockapi.getFood() }
        assert(response.isSuccessful)

        val foodlistRes = response.body()?.toFoodList()

        assert(foodlistRes != null)
        foodlistRes!!
        val lastId = foodlistRes.last().foodId

        // put food
        assert(runBlocking {
            stockapi.putFood(lastId, food.toJsonObject())
        }.isSuccessful)

        val stock = Stock(-1, lastId, 1000f, LocalDate.now().plusYears(1)).toJsonObject()
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
        assert(runBlocking { stockapi.deleteStock(lastStockId).isSuccessful })
        assert(runBlocking { stockapi.deleteFood(lastId).isSuccessful })
    }

    private val stockRepository = StockRepository(stockapi)

    @Test
    fun testStockRepository() {

        val food = Food("test", -1, "test", "l")

        assert(runBlocking { stockRepository.postFood(food) }.isSuccess())

        val Foodres = runBlocking { stockRepository.getFood() }
        assert(Foodres.isSuccess())

        val foodList = Foodres.data
        assert(foodList != null)
        foodList!!
        val foodLast = foodList.last().foodId

        assert(runBlocking { stockRepository.getFood(foodLast) }.isSuccess())
        // assert(runBlocking { stockRepository.putFood(food) }.isSuccess())


        val stock = Stock(-1, foodLast, 1000f, LocalDate.now())

        assert(runBlocking { stockRepository.postStock(stock).isSuccess() })
        val stockRes = runBlocking {
            stockapi.getStock()
        }
        assert(stockRes.isSuccessful)


        //assert(runBlocking { stockRepository.getStock(foodLast).isSuccess() })
        assert(runBlocking { stockRepository.getFoodStock(foodList.last()).isSuccess() })

        //assert(runBlocking {  stockRepository.putStock(stock).isSuccess() })
        //assert(runBlocking { stockRepository.deleteStock(stock).isSuccess() })
        //assert(runBlocking { stockRepository.deleteFood(food).isSuccess() })

    }

    private val recipeApi = retrofit.create(RecipeInterface::class.java)

    private val recipeModel = Recipe(name = "Unit Test", type = Recipe.RecipeType.NONE, recipeId = 1, price = 1f, available = true, foodType = 0)

    @Test
    fun testRecipe() {
        assert(runBlocking {
            recipeApi.postRecipe(recipeModel.toJsonObject())
        }.isSuccessful)

        val r = runBlocking { recipeApi.getRecipes() }

        assert(r.isSuccessful)
        val o = r.body()?.toRecipeList()

        assert(o != null)
        o!!
        val lastId = o.last().recipeId

        assert(runBlocking { recipeApi.getRecipe(lastId).isSuccessful })
        assert(runBlocking { recipeApi.putRecipe(lastId, recipeModel.toJsonObject()).isSuccessful })
        assert(runBlocking { recipeApi.deleteRecipe(lastId).isSuccessful })
    }

    private val recipeRepository = RecipeRepository(recipeApi)

    @Test
    fun testRecipeRepository() {

        assert(runBlocking { recipeRepository.postRecipe(recipeModel).isSuccess() })
        val recipeListRes = runBlocking { recipeRepository.getRecipes() }

        assert(recipeListRes.isSuccess())

        val recipeList = recipeListRes.data!!
        val recipeLast = recipeList.last()

        assert(runBlocking { recipeRepository.getRecipe(recipeLast.recipeId).isSuccess() })
        assert(runBlocking { recipeRepository.putRecipe(recipeLast).isSuccess() })
        assert(runBlocking { recipeRepository.deleteRecipe(recipeLast.recipeId).isSuccess() })

    }


    private val orderApi = retrofit.create(OrdersInterface::class.java)

    private fun createBooking(): Int {
        val bookingObject = BookingObject(name = "Unit Test", email = "Uni@a", phone = "1", date = LocalDateTime.now(), people = 1, table = "1")

        val bookingGetRes = runBlocking {
            assert(bookingApi.postBookings(bookingObject = bookingObject).isSuccessful)
            val res = bookingApi.getBookings()
            assert(res.isSuccessful)
            res
        }

        val bookingList = bookingGetRes.body()?.toBookingList()
        val lastId = bookingList?.last()?.bookingId

        return lastId!!
    }

    private fun createRecipe(): Int {

        // create food
        val food = Food("Alimentos carnicos", -1, "test", "l")

        // insert food
        assert(runBlocking { stockapi.postFood(food.toJsonObject()) }.isSuccessful)

        // get food list
        val response = runBlocking { stockapi.getFood() }
        assert(response.isSuccessful)

        val foodlistRes = response.body()?.toFoodList()

        assert(foodlistRes != null)
        foodlistRes!!
        val lastId = foodlistRes.last().foodId

        // add stock
        val stock = Stock(-1, lastId, 1000f, LocalDate.now().plusYears(1)).toJsonObject()
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

        val r = runBlocking {
            assert(recipeApi.postRecipe(Recipe(name = "Unit Test",
                type = Recipe.RecipeType.NONE,
                recipeId = 1,
                price = 1f,
                available = true,
                foodType = 0, food = listOf(Recipe.RecipeFood(foodId = lastId, quantity = 1.0f))).toJsonObject()).isSuccessful)
            val res = recipeApi.getRecipes()
            assert(res.isSuccessful)
            res
        }
        val o = r.body()?.toRecipeList()
        return o!!.last().recipeId
    }

    @Test
    fun testOrder() {
        val id = createBooking()
        val recipeId = createRecipe()
        val orderJson =
            Order(bookingId = id,
                recipeList = listOf(Order.OrderRecipe(recipeId = recipeId, quantity = 1.0f, price = 12.0f, type = 0, name = "name"))).toJsonObject()

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


    private val orderRepository = OrdersRepository(orderApi)

    @Test
    fun testOrderRepository() {
        val bookingId = createBooking()
        val recipeId = createRecipe()
        val order = Order(bookingId = bookingId,
            recipeList = listOf(Order.OrderRecipe(recipeId = recipeId, quantity = 1.0f, price = 1.0f, type = 0, name = "name")))

        assert(runBlocking { orderRepository.postOrder(order).isSuccess() })

        val orderListRes = runBlocking { orderRepository.getOrders() }

        assert(orderListRes.isSuccess())

        val orderList = orderListRes.data!!
        val orderLast = orderList.last()

        assert(runBlocking { orderRepository.getOrder(orderLast.orderId).isSuccess() })
        assert(runBlocking { orderRepository.putOrder(orderLast).isSuccess() })
        assert(runBlocking { orderRepository.deleteOrder(orderLast.orderId).isSuccess() })

        assert(runBlocking { orderRepository.getOrderByStatus(Order.Status.Waiting()).isSuccess() })
        assert(runBlocking { orderRepository.getOrderByStatus(Order.Status.Confirmed()).isSuccess() })
        assert(runBlocking { orderRepository.getOrderByStatus(Order.Status.Delievered()).isSuccess() })
        assert(runBlocking { orderRepository.getOrderByStatus(Order.Status.Paid()).isSuccess() })
        assert(runBlocking { orderRepository.getOrderByStatus(Order.Status.Cancelled()).isSuccess() })
        assert(runBlocking { orderRepository.getOrderByStatus(Order.Status.InProcess()).isSuccess() })
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

        val lastId = bookingList?.last()?.bookingId

        assert(lastId != null)
        lastId!!

        assert(runBlocking { bookingApi.getBooking(lastId).isSuccessful })
        assert(runBlocking { bookingApi.putBooking(lastId, bookingObject).isSuccessful })
        assert(runBlocking { bookingApi.deleteBooking(lastId) }.isSuccessful)
    }

    private val BookingRepository = com.srm.srmapp.repository.bookings.BookingRepository(bookingApi)

    @Test
    fun testBookingRepository() {

        val bookingObject = Booking(name = "b", email = "a@a", phone = "1", date = LocalDateTime.now(), people = 1, table = "1")
        val bokPostRes = runBlocking { BookingRepository.postBooking(bookingObject) }
        assert(bokPostRes.isSuccess())

        val bookingGetRes = runBlocking { BookingRepository.getBookings() }
        assert(bookingGetRes.isSuccess())

        val booklist = bookingGetRes.data
        assert(booklist != null)

        val lastId = booklist?.last()?.bookingId
        assert(lastId != null)
        lastId!!

        assert(runBlocking { BookingRepository.getBooking(lastId).isSuccess() })
        assert(runBlocking { BookingRepository.putBooking(lastId, bookingObject).isSuccess() })
        assert(runBlocking { BookingRepository.deleteBooking(lastId).isSuccess() })
    }

    interface TestInterface {
        @GET("/error404")
        suspend fun getError404(): Response<String>

        @GET("/")
        suspend fun getHTMLContent(): Response<String>

        @GET("/api/v1/user")
        suspend fun getUser(): Response<UserResponse>
    }

    private val safeCallObject = object : BaseRepository() {
        val authorized = retrofit.create(TestInterface::class.java)
    }

    @Test
    fun testSafeCall() {
        safeCallObject.apply {

            val success1 = runBlocking { safeApiCall({ authorized.getUser() }) { "" } }
            assert(success1.isSuccess())

            val success2 = runBlocking { safeApiCall({ authorized.getHTMLContent() }) { "" } }
            Timber.i("$success2")
            assert(success2.isError())
        }
    }
}