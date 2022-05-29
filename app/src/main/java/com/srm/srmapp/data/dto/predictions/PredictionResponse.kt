package com.srm.srmapp.data.dto.predictions


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PredictionResponse(
    @SerializedName("persons")
    val persons: Float, // 3
    @SerializedName("recipe")
    val recipe: String, // Vegetal
)