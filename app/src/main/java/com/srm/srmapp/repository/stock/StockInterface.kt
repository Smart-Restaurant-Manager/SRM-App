package com.srm.srmapp.repository.stock

import com.srm.srmapp.data.dto.stock.response.FoodListResponse
import com.srm.srmapp.data.dto.stock.response.FoodResponse
import com.srm.srmapp.data.dto.stock.response.StockListResponse
import com.srm.srmapp.data.dto.stock.response.StockResponse
import retrofit2.Response
import retrofit2.http.*

interface StockInterface {
    @GET("api/fake/food")
    suspend fun getFood(): Response<FoodListResponse>

    @POST("api/fake/food")
    suspend fun postFood(): Response<Unit>

    @GET("api/fake/food/{food}")
    suspend fun getFood(@Path("food") id: Int): Response<FoodResponse>

    @PUT("api/fake/food/{food}")
    suspend fun modifyFood(@Path("food") id: Int): Response<Unit>

    @DELETE("api/fake/food/{food}")
    suspend fun deleteFood(@Path("food") id: Int): Response<Unit>

    @GET("api/fake/food/{food}/stock")
    suspend fun getFoodStock(@Path("food") id: Int): Response<StockListResponse>

    @GET("api/fake/stocks")
    suspend fun getStock(): Response<StockListResponse>

    @POST("api/fake/stocks")
    suspend fun postStock(): Response<StockListResponse>

    @GET("api/fake/stocks/{stock}")
    suspend fun getStock(@Path("stock") id: Int): Response<StockResponse>

    @DELETE("api/fake/stocks/{stock}")
    suspend fun deleteStock(@Path("stock") id: Int): Response<Unit>

    @PUT("api/fake/stocks/{stock}")
    suspend fun modifyStock(@Path("stock") id: Int): Response<Unit>
}