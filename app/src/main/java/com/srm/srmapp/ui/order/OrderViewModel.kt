package com.srm.srmapp.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.srm.srmapp.Resource
import com.srm.srmapp.Utils.fetchResource
import com.srm.srmapp.data.models.Order
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.repository.orders.OrdersRepository
import com.srm.srmapp.repository.recipes.RecipeRepository
import com.srm.srmapp.repository.stock.StockRepository
import com.srm.srmapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val api: OrdersRepository,
    private val recipeApi: RecipeRepository,
    private val foodApi: StockRepository,
) : BaseViewModel() {
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

    fun changeOrderStatus(status: Order.Status) {
        _order.value?.data?.let { order ->
            order.status = status
            fetchResource(_status,
                onSuccess = { _ ->
                    _order.postValue(Resource.Success(order))
                }) {
                api.putOrder(order)
            }
        }
    }

    suspend fun getFoodName(id: Int) {
        foodApi.getFood(id)
    }
}