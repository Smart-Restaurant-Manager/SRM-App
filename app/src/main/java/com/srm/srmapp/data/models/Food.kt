package com.srm.srmapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class FoodType {
    NONE,
    CARNE, LACTEOS,
    ESPECIAS, VEGETALES,
    CEREALEs, MARISCOS
}

@Parcelize
data class Food(val type: FoodType, val id: Int, val name: String, val units: String, val stock: List<Stock>? = null) : Parcelable
