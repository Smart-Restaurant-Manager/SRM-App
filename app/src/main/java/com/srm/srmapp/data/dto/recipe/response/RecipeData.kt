package com.srm.srmapp.data.dto.recipe.response

import com.srm.srmapp.data.models.Recipe

data class RecipeData(
    val attributes: RecipeAttributes,
    val id: Int,
    val type: String,
){
    fun toRecipe() = Recipe(
        type = when(type){
        "Entrante" -> Recipe.RecipeType.ENTRANTE
        "FirstPlate" -> Recipe.RecipeType.FIRST_PLATE
        "SecondPlate" -> Recipe.RecipeType.SECOND_PLATE
        "Desert" -> Recipe.RecipeType.DESERT
        "Bebida" -> Recipe.RecipeType.DRINK
        "Complementos" -> Recipe.RecipeType.COMPLEMENTS
        else -> Recipe.RecipeType.NONE
    }, id =id, name = attributes.name, price =  attributes.price)
}