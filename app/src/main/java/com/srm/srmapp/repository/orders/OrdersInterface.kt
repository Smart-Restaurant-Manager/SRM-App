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

    @GET("api/v1/orders/status/waiting")
    suspend fun getOrdersWaiting(): Response<OrderListResponse>

    @GET("api/v1/orders/status/confirmed")
    suspend fun getOrdersConfirmed(): Response<OrderListResponse>

    @GET("api/v1/orders/status/cancelled")
    suspend fun getOrdersCanelled(): Response<OrderListResponse>

    @GET("api/v1/orders/status/in-process")
    suspend fun getOrdersInProgress(): Response<OrderListResponse>

    @GET("api/v1/orders/status/delievered")
    suspend fun getOrdersDelievered(): Response<OrderListResponse>

    @GET("api/v1/orders/status/paid")
    suspend fun getOrdersPaid(): Response<OrderListResponse>
}