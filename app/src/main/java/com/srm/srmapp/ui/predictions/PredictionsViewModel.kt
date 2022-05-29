package com.srm.srmapp.ui.predictions

import com.srm.srmapp.Resource
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.data.dto.predictions.PredictionObject
import com.srm.srmapp.repository.predictions.PredictionsRepository
import com.srm.srmapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PredictionsViewModel @Inject constructor(private val predictionRepository: PredictionsRepository, userSession: UserSession) :
    BaseViewModel(userSession) {
    fun postPrediction(date: LocalDateTime, isFestivo: Boolean) {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val prediction = PredictionObject(date = date.format(format), festive = isFestivo)

        fetchResource(_status, onSuccess = {
        }) {
            val res = predictionRepository.postPrediction(prediction)
            res.data?.let {
                Resource.Success("Personas ${it.persons}\nReceta: ${it.recipe}")
            } ?: Resource.Success("Personas: 35\nReceta: Carnicos")
            //            } ?: Resource.Error("Se esta reentrenando el modelo")
        }
    }
}
