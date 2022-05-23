package com.srm.srmapp.ui.predictions

import com.srm.srmapp.data.UserSession
import com.srm.srmapp.data.dto.predictions.PredictionObject
import com.srm.srmapp.repository.predictions.PredictionsRepository
import com.srm.srmapp.ui.common.BaseViewModel
import javax.inject.Inject

class PredictionsViewModel @Inject constructor(private val predictionRepository: PredictionsRepository, userSession: UserSession) :
    BaseViewModel(userSession) {
    fun postPrediction(predictions: PredictionObject) {
        fetchResource(_status, onSuccess = {
        }) {
            predictionRepository.postPrediction(predictions)
        }
    }
}
