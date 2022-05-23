package com.srm.srmapp.data.dto.predictions

import com.google.gson.annotations.SerializedName
import java.util.*

data class PredictionObject(
    @SerializedName("date")
    val date: String, // a
    @SerializedName("festive")
    val festive: Boolean, // a@a.a
)