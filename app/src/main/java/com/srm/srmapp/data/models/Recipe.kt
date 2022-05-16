package com.srm.srmapp.data.models

import android.os.Parcelable
import com.srm.srmapp.R
import com.srm.srmapp.data.dto.recipe.body.RecipeFoodObject
import com.srm.srmapp.data.dto.recipe.body.RecipeObject
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val type: RecipeType,
    val recipeId: Int = -1,
    val name: String,
    val price: Float,
    val available: Boolean? = null,
    val food: List<Pair<Int, Float>>? = null, // Food_id and quantity
) : Parcelable, GetId {
    enum class RecipeType {
        NONE, ENTRANTE, FIRST_PLATE,
        SECOND_PLATE, DESERT, DRINK,
        COMPLEMENTS
    }

    fun toInt() = when (type) {
        RecipeType.ENTRANTE -> 0
        RecipeType.FIRST_PLATE -> 1
        RecipeType.SECOND_PLATE -> 2
        RecipeType.DESERT -> 3
        RecipeType.DRINK -> 4
        RecipeType.COMPLEMENTS -> 5
        else -> -1
    }

    fun toJsonObject() =
        RecipeObject(name = name, price = price, available = available, type = this.toInt(), food = food?.map { it.toRecipeFoodObject() })

    override fun getId(): Int {
        return recipeId
    }

    override fun toString(): String {
        return "$name  $recipeId  $price $food"
    }

    companion object {
        fun getResource(type: RecipeType): Int = when (type) {
            RecipeType.ENTRANTE -> R.string.entrantes
            RecipeType.FIRST_PLATE -> R.string.first_plate
            RecipeType.SECOND_PLATE -> R.string.second_plate
            RecipeType.DESERT -> R.string.deserts
            RecipeType.DRINK -> R.string.drinks
            RecipeType.COMPLEMENTS -> R.string.complementos
            RecipeType.NONE -> R.string.error_type
        }

        fun getResource(id: Int) = when (id) {
            0 -> R.string.entrantes
            1 -> R.string.first_plate
            2 -> R.string.second_plate
            3 -> R.string.deserts
            4 -> R.string.drinks
            5 -> R.string.complementos
            else -> R.string.error_type
        }

        val RECIPE_ERROR = Recipe(Recipe.RecipeType.NONE, -1, "Error", 0f)
    }
}

fun Pair<Int, Float>.toRecipeFoodObject() = RecipeFoodObject(food_id = this.first, this.second)
