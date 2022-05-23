package com.srm.srmapp.data.models

import com.srm.srmapp.data.dto.predictions.PredictionObject

data class Predictions(
    val predictionId: Int = -1,
    val date: String,
    val festive: Boolean
    ) : GetId{
        fun toJsonObject(): PredictionObject {
            return PredictionObject(date = date, festive = festive)
        }

    override fun getId(): Int {
        return predictionId
    }
}