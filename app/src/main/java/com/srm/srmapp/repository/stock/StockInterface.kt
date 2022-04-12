package com.srm.srmapp.repository.stock

import com.srm.srmapp.data.dto.stock.response.FoodListResponse
import com.srm.srmapp.data.dto.stock.response.FoodResponse
import com.srm.srmapp.data.dto.stock.response.StockListResponse
import com.srm.srmapp.data.dto.stock.response.StockResponse
import retrofit2.Response
import retrofit2.http.*

interface StockInterface {
    @GET("api/v1/food")
    suspend fun getFood(): Response<FoodListResponse>

    @POST("api/v1/food")
    suspend fun postFood(): Response<Unit>// TODO BODY

    @GET("api/v1/food/{food}")
    suspend fun getFood(@Path("food") id: Int): Response<FoodResponse>

    @PUT("api/v1/food/{food}")
    suspend fun modifyFood(@Path("food") id: Int): Response<Unit> // TODO BODY

    @DELETE("api/v1/food/{food}")
    suspend fun deleteFood(@Path("food") id: Int): Response<Unit>

    @GET("api/v1/food/{food}/stock")
    suspend fun getFoodStock(@Path("food") id: Int): Response<StockListResponse>

    @GET("api/v1/stocks")
    suspend fun getStock(): Response<StockListResponse>

    @POST("api/v1/stocks")
    suspend fun postStock(): Response<StockListResponse> // TODO BODY

    @GET("api/v1/stocks/{stock}")
    suspend fun getStock(@Path("stock") id: Int): Response<StockResponse>

    @DELETE("api/v1/stocks/{stock}")
    suspend fun deleteStock(@Path("stock") id: Int): Response<Unit>

    @PUT("api/v1/stocks/{stock}")
    suspend fun modifyStock(@Path("stock") id: Int): Response<Unit> // TODO BODY
}