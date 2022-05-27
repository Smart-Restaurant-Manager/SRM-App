package com.srm.srmapp.repository.predictions

import com.srm.srmapp.data.dto.predictions.PredictionObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PredictionsInterface {

    @GET("api/v1/ai/retrain-customers")
    suspend fun getCostumers(@Body predictionObject: PredictionObject): Response<Unit>

    @GET("api/v1/ai/retrain-food")
    suspend fun getFood(@Body predictionObject: PredictionObject): Response<Unit>


    @POST("api/v1/ai/predictions")
    suspend fun postPrediction(@Body predictionObject: PredictionObject): Response<Unit>





}