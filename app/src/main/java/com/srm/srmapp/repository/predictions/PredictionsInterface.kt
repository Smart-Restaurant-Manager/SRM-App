package com.srm.srmapp.repository.predictions

import com.srm.srmapp.data.dto.predictions.PredictionObject
import com.srm.srmapp.data.dto.predictions.PredictionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PredictionsInterface {
    @POST("api/v1/ai/predictions")
    suspend fun postPrediction(@Body predictionObject: PredictionObject): Response<PredictionResponse>


}