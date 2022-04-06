package com.srm.srmapp.data.models

import android.os.Parcelable
import com.srm.srmapp.R
import kotlinx.parcelize.Parcelize
import timber.log.Timber


@Parcelize
data class Food(val type: FoodType, val id: Int, val name: String, val units: String, val stock: List<Stock>? = null) : Parcelable {
    enum class FoodType {
        NONE,
        CARNE, LACTEOS,
        ESPECIAS, VEGETALES,
        CEREALES, MARISCOS
    }

    companion object {
        fun parseId(id: Int) = when (id) {
            R.id.btCarne -> FoodType.CARNE
            R.id.btCereales -> FoodType.CEREALES
            R.id.btMariscos -> FoodType.MARISCOS
            R.id.btEspecias -> FoodType.ESPECIAS
            R.id.btVegetales -> FoodType.VEGETALES
            R.id.btLacteos -> FoodType.LACTEOS
            else -> {
                Timber.w("Unkown id $id")
                FoodType.NONE
            }
        }
    }

    override fun toString(): String {
        return "$name $units $id"
    }
}
