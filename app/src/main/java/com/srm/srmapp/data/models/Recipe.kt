package com.srm.srmapp.data.models

import com.srm.srmapp.R
import com.srm.srmapp.data.dto.recipe.body.RecipeObject

data class Recipe(
    val type: RecipeType,
    val recipeId: Int = -1,
    val foodType: Int,
    val name: String,
    val price: Float,
    val available: Boolean = true,
    val food: List<RecipeFood> = emptyList(),
) : GetId {
    data class RecipeFood(val foodId: Int, val name: String = "", val quantity: Float, val unit: String = "") : GetId {
        override fun getId(): Int {
            return foodId
        }
    }

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
        RecipeObject(name = name,
            price = price,
            available = available,
            foodType = foodType,
            type = this.toInt(),
            food = food.map { it.toRecipeFoodObject() })

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
    }
}

fun Recipe.RecipeFood.toRecipeFoodObject() = RecipeObject.Food(foodId = foodId, quantity = quantity)
