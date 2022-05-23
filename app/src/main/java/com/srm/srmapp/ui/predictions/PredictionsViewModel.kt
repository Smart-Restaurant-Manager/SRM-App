package com.srm.srmapp.ui.predictions

import androidx.lifecycle.MutableLiveData
import com.srm.srmapp.Resource
import com.srm.srmapp.data.dto.predictions.PredictionObject
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Predictions
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.repository.predictions.PredictionsRepository

import com.srm.srmapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class PredictionsViewModel @Inject constructor(private val predictionRepository: PredictionsRepository) : BaseViewModel() {
    fun postPrediction(predictions: PredictionObject) {
        fetchResource(_status, onSuccess = {
        }) {
            predictionRepository.postPrediction(predictions)
        }
    }
}
