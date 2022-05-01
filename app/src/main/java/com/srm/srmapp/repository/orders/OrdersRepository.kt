package com.srm.srmapp.repository.orders

import com.srm.srmapp.data.dto.orders.body.OrderObject
import com.srm.srmapp.data.dto.orders.response.toOrder
import com.srm.srmapp.data.dto.orders.response.toOrderList
import com.srm.srmapp.data.models.Order
import com.srm.srmapp.repository.BaseRepository
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

    suspend fun postOrder(orderObject: OrderObject) = safeApiCall({
        ordersInterface.postOrder(orderObject)
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


    suspend fun getOrderByStatus(status: String) = safeApiCall({
        ordersInterface.getOrdersWaiting(status)
    }) {
        it.toOrderList()
    }
}