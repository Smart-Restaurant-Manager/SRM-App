package com.srm.srmapp.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.srm.srmapp.Resource
import com.srm.srmapp.Utils.fetchResource
import com.srm.srmapp.data.models.Order
import com.srm.srmapp.repository.orders.OrdersInterface
import com.srm.srmapp.repository.orders.OrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class OrderViewmodel @Inject constructor(private val ordersRepository: OrdersRepository) : ViewModel() {
    private val _orderList: MutableLiveData<Resource<List<Order>>> = MutableLiveData()
    val orderList: LiveData<Resource<List<Order>>>
        get() = _orderList

    private val _order: MutableLiveData<Resource<Order>> = MutableLiveData()
    val order: LiveData<Resource<Order>>
        get() = _order
    private val _status: MutableLiveData<Resource<String>> = MutableLiveData()
    val status: LiveData<Resource<String>>
        get() = _status


    fun refreshOrderList() {
        Timber.d("Call refresh")
        fetchResource(_orderList) {
            ordersRepository.getOrders()
        }
    }

    fun getOrderBy(id: Int){
        fetchResource(_order){
            ordersRepository.getOrder(id)
        }
    }
    fun addOrder(order: Order){
        fetchResource(_status, onSuccess = {
            refreshOrderList()
        }){
            ordersRepository.postOrder((order))
        }
    }

    fun deleteOrder(id: Int){
        fetchResource(_status, onSuccess = {
            _orderList.value?.data?.toMutableList()?.let {
                list -> list.removeIf{it.orderId == id}
                _orderList.postValue(Resource.Success(list.toList()))
            }
        }){
            ordersRepository.deleteOrder(id)
        }
    }

    fun getOrdersByStatus(status: Order.Status){
        fetchResource(_orderList){
            ordersRepository.getOrderByStatus(status)
        }
    }

    fun editOrder(order: Order){
        fetchResource(_status){
            ordersRepository.putOrder(order)
        }
    }



}

