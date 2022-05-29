package com.srm.srmapp.data.dto.predictions

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PredictionObject(
    @SerializedName("date")
    val date: String, // a
    @SerializedName("festive")
    val festive: Boolean, // a@a.a
)