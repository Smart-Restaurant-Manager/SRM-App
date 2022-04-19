package com.srm.srmapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import timber.log.Timber

@Parcelize
data class Recipe(val type: RecipeType, val id: Int, val name: String, val price: Float, val food: List<Food>? = null) : Parcelable {
    enum class RecipeType {
        NONE, ENTRANTE, FIRST_PLATE,
        SECOND_PLATE, DESERT, DRINK,
        COMPLEMENTS
    }

    companion object {
        fun parseId(id: Int) = when (id) {

            else -> {
                Timber.w("Unkown id $id")
                Recipe.RecipeType.NONE
            }
        }
    }

    override fun toString(): String {
        return "$name  $id  $price "
    }

}