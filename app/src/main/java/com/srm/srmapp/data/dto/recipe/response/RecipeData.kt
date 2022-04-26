package com.srm.srmapp.data.dto.recipe.response

import androidx.annotation.Keep
import com.srm.srmapp.data.models.Recipe

@Keep
data class RecipeData(
    val attributes: RecipeAttributes,
    val id: Int,
    val type: String,
)

fun RecipeData.toRecipe() = Recipe(
    type = when (attributes.type) {
        0 -> Recipe.RecipeType.ENTRANTE
        1 -> Recipe.RecipeType.FIRST_PLATE
        2 -> Recipe.RecipeType.SECOND_PLATE
        3 -> Recipe.RecipeType.DESERT
        4 -> Recipe.RecipeType.DRINK
        5 -> Recipe.RecipeType.COMPLEMENTS
        else -> Recipe.RecipeType.NONE
    }, id = id,
    name = attributes.name,
    price = attributes.price,
    available = attributes.available)
