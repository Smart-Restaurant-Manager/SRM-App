package com.srm.srmapp.repository.predictions

import com.srm.srmapp.data.dto.predictions.PredictionObject
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Predictions
import com.srm.srmapp.repository.BaseRepository
import javax.inject.Inject

class PredictionsRepository @Inject constructor(private val api: PredictionsInterface) : BaseRepository() {

    suspend fun postPrediction(predictions: PredictionObject) = safeApiCall({
        api.postPrediction(predictions)
    }) {
        it
    }
}