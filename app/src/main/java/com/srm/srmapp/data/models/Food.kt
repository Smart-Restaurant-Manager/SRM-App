package com.srm.srmapp.data.models

import android.os.Parcelable
import com.srm.srmapp.R
import com.srm.srmapp.data.dto.stock.body.FoodObject
import kotlinx.parcelize.Parcelize
import timber.log.Timber


@Parcelize
data class Food(val type: FoodType, var foodId: Int, val name: String, val units: String, val stockList: MutableList<Stock> = mutableListOf()) :
    Parcelable {
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
        return "$name $units $foodId"
    }

    fun toJsonObject(): FoodObject = FoodObject(name, units)
    fun addStock(stock: Stock) {
        stock.foodId = this.foodId
        stockList.add(stock)
    }
}
