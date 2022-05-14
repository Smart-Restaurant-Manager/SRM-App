package com.srm.srmapp.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.srm.srmapp.AppModule
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Order
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.repository.orders.OrdersRepository
import com.srm.srmapp.repository.recipes.RecipeRepository
import com.srm.srmapp.repository.stock.StockRepository
import com.srm.srmapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val api: OrdersRepository,
    private val recipeApi: RecipeRepository,
    private val foodApi: StockRepository,
) : BaseViewModel() {
    val predicate: (Order, String) -> Boolean = { order, query ->
        val time = try {
            val timequery = LocalTime.from(AppModule.timeFormatter.parse(query))
            val time = order.booking?.date?.toLocalTime()
            time?.isAfter(timequery)
        } catch (e: DateTimeParseException) {
            Timber.d("Not a time query")
            null
        } ?: false

        val date = try {
            val dateQuery = LocalDate.from(AppModule.dateFormatter.parse(query))
            val date = order.booking?.date?.toLocalDate()
            date?.isAfter(dateQuery)
        } catch (e: DateTimeParseException) {
            Timber.d("Not a date query")
            null
        } ?: false

        val dateTime = try {
            val dateTimeQuery = LocalDateTime.from(AppModule.dateTimeFormatter.parse(query))
            val dateTime = order.booking?.date
            dateTime?.isAfter(dateTimeQuery)
        } catch (e: DateTimeParseException) {
            Timber.d("Not a date time query")
            null
        } ?: false


        val email = order.booking?.email?.startsWith(query, ignoreCase = false) ?: false
        val id = order.orderId.toString().startsWith(query, ignoreCase = true)
        val table = order.booking?.table?.startsWith(query, ignoreCase = false) ?: false
        val name = order.booking?.name?.startsWith(query, ignoreCase = false) ?: false
        val isAfter = dateTime || date || time

        isAfter || id || name || email || table
    }
    private val _orderList = MutableLiveData<Resource<List<Order>>>(Resource.Empty())
    val orderList: LiveData<Resource<List<Order>>>
        get() = _orderList

    private val _orderStatus = MutableLiveData<Order.Status>()
    val orderStatus: LiveData<Order.Status>
        get() = _orderStatus

    private val _order = MutableLiveData<Resource<Order>>(Resource.Empty())
    val order: LiveData<Resource<Order>>
        get() = _order

    private val _recipe = MutableLiveData<Resource<Recipe>>(Resource.Empty())
    val recipe: LiveData<Resource<Recipe>>
        get() = _recipe

    fun clearOrder() {
        _order.value = Resource.Empty()
    }

    fun setOrderStatus(status: Order.Status) {
        _orderStatus.value = status
    }

    fun clearOrderStatus() {
        _orderStatus.value = Order.Status.None()
    }

    fun refreshOrder() {
        fetchResource(_orderList) {
            orderStatus.value?.let {
                if (it is Order.Status.None)
                    return@let api.getOrders()
                api.getOrderByStatus(it)
            } ?: api.getOrders()
        }
    }

    fun getOrderRecipe(order: Order) {
        fetchResource(_order) {
            api.getOrder(order.orderId)
        }
    }

    fun getRecipe(recipeId: Int) {
        fetchResource(_recipe) {
            recipeApi.getRecipe(recipeId)
        }
    }

    fun changeOrderStatus(order: Order, status: Order.Status) {
        fetchResource(_status,
            onSuccess = { _ ->
                _order.postValue(Resource.Success(order))
                _orderList.value?.data?.let {
                    it.toMutableList().apply {
                        remove(first { it.orderId == order.orderId })
                        add(order)
                        _orderList.postValue(Resource.Success(this.toList()))
                    }
                }
            }) {
            order.status = status
            api.putOrder(order)
        }
    }

    fun deleteOrder(order: Order) {
        fetchResource(_status) {
            api.deleteOrder(order.orderId)
        }
    }

    suspend fun getFoodName(id: Int) {
        foodApi.getFood(id)
    }
}