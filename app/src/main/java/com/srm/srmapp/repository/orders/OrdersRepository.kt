package com.srm.srmapp.repository.orders

import com.srm.srmapp.data.dto.orders.response.toOrder
import com.srm.srmapp.data.dto.orders.response.toOrderList
import com.srm.srmapp.data.models.Order
import com.srm.srmapp.repository.BaseRepository
import timber.log.Timber
import javax.inject.Inject

class OrdersRepository @Inject constructor(private val ordersInterface: OrdersInterface) : BaseRepository() {
    suspend fun getOrders() = safeApiCall({
        ordersInterface.getOrders()
    }) {
        it.toOrderList()
    }

    suspend fun getOrder(id: Int) = safeApiCall({
        ordersInterface.getOrder(id)
    }) {
        it.toOrder()
    }

    suspend fun postOrder(order: Order) = safeApiCall({
        ordersInterface.postOrder(order.toJsonObject())
    }) {
        "Order added"
    }

    suspend fun deleteOrder(id: Int) = safeApiCall({
        ordersInterface.deleteOrder(id)
    }) {
        "Order deleted"
    }

    suspend fun putOrder(order: Order) = safeApiCall({
        ordersInterface.putOrder(order.orderId, order.toJsonObject())
    }) {
        "Order modified"
    }


    suspend fun getOrderByStatus(status: Order.Status) = safeApiCall({
        if (status is Order.Status.None)
            Timber.e("Requesting order by None")
        ordersInterface.getOrdersWaiting(status.toString())
    }) {
        it.toOrderList()
    }
}