package com.srm.srmapp.repository.orders

import com.srm.srmapp.data.dto.orders.body.OrderObject
import com.srm.srmapp.data.dto.orders.response.OrderListResponse
import com.srm.srmapp.data.dto.orders.response.OrderResponse
import retrofit2.Response
import retrofit2.http.*

interface OrdersInterface {
    @GET("api/v1/orders")
    suspend fun getOrders(): Response<OrderListResponse>

    @POST("api/v1/orders")
    suspend fun postOrder(@Body orderObject: OrderObject): Response<Unit>

    @GET("api/v1/orders/{order}")
    suspend fun getOrder(@Path("order") order_id: Int): Response<OrderResponse>

    @PUT("api/v1/orders/{order}")
    suspend fun putOrder(@Path("order") order_id: Int, @Body orderObject: OrderObject): Response<Unit>

    @DELETE("api/v1/orders/{order}")
    suspend fun deleteOrder(@Path("order") order_id: Int): Response<Unit>

    @GET("api/v1/orders/status/{status}")
    suspend fun getOrdersWaiting(status: String): Response<OrderListResponse>

    companion object STATUS {
        const val WAITING = "waiting"
        const val CONFIRMED = "confirmed"
        const val CANCELLED = "cancelled"
        const val IN_PROCESS = "in-process"
        const val DELIEVERED = "delievered"
        const val PAID = "paid"
    }
}