package com.srm.srmapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(val type: RecipeType, val id: Int, val name: String, val price: Float, val food: List<Food>? = null) : Parcelable {
    enum class RecipeType {
        NONE, ENTRANTE, FIRST_PLATE,
        SECOND_PLATE, DESERT, DRINK,
        COMPLEMENTS
    }

    override fun toString(): String {
        return "$name  $id  $price "
    }
}