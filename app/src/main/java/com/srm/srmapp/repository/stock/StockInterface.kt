package com.srm.srmapp.repository.stock

import com.srm.srmapp.data.dto.stock.body.FoodObject
import com.srm.srmapp.data.dto.stock.body.StockObject
import com.srm.srmapp.data.dto.stock.response.FoodListResponse
import com.srm.srmapp.data.dto.stock.response.FoodResponse
import com.srm.srmapp.data.dto.stock.response.StockListResponse
import com.srm.srmapp.data.dto.stock.response.StockResponse
import retrofit2.Response
import retrofit2.http.*

interface StockInterface {
    @GET("api/v1/food")
    suspend fun getFood(): Response<FoodListResponse>

    @GET("api/v1/food/{food}")
    suspend fun getFood(@Path("food") id: Int): Response<FoodResponse>

    @GET("api/v1/food/{food}/stocks")
    suspend fun getFoodStock(@Path("food") id: Int): Response<StockListResponse>

    @GET("api/v1/stocks")
    suspend fun getStock(): Response<StockListResponse>

    @GET("api/v1/stocks/{stock}")
    suspend fun getStock(@Path("stock") id: Int): Response<StockResponse>

    @POST("api/v1/food")
    suspend fun postFood(@Body foodObject: FoodObject): Response<Unit>

    @PUT("api/v1/food/{food}")
    suspend fun putFood(@Path("food") id: Int, @Body foodObject: FoodObject): Response<Unit>

    @DELETE("api/v1/food/{food}")
    suspend fun deleteFood(@Path("food") id: Int): Response<Unit>

    @POST("api/v1/stocks")
    suspend fun postStock(@Body stockObject: StockObject): Response<Unit>

    @DELETE("api/v1/stocks/{stock}")
    suspend fun deleteStock(@Path("stock") id: Int): Response<Unit>

    @PUT("api/v1/stocks/{stock}")
    suspend fun putStock(@Path("stock") id: Int, @Body stockObject: StockObject): Response<Unit>
}