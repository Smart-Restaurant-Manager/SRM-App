package com.srm.srmapp.data.dto.recipe.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.srm.srmapp.data.models.Recipe
import java.time.LocalDateTime

@Keep
data class RecipeResponse(
    @SerializedName("data")
    val `data`: Data,
) {
    data class Data(
        @SerializedName("type")
        val type: String, // recipes
        @SerializedName("id")
        val id: Int, // 811
        @SerializedName("food_type")
        val foodType: Int, // 811
        @SerializedName("attributes")
        val attributes: Attributes,
    ) {
        data class Attributes(
            @SerializedName("name")
            val name: String, // ee
            @SerializedName("price")
            val price: Float, // 12
            @SerializedName("type")
            val type: Int, // 0
            @SerializedName("available")
            val available: Boolean, // true
            @SerializedName("food")
            val food: List<Food>,
            @SerializedName("created_at")
            val createdAt: LocalDateTime, // 2022-05-07T11:16:29.000000Z
            @SerializedName("updated_at")
            val updatedAt: LocalDateTime, // 2022-05-07T11:16:29.000000Z
        ) {
            data class Food(
                @SerializedName("type")
                val type: String, // food
                @SerializedName("id")
                val id: Int, // 261
                @SerializedName("attributes")
                val attributes: Attributes,
            ) {
                data class Attributes(
                    @SerializedName("name")
                    val name: String, // Patatos
                    @SerializedName("units")
                    val units: String, // Kg
                    @SerializedName("type")
                    val type: String, // Especias
                    @SerializedName("quantity")
                    val quantity: Float, // 5
                    @SerializedName("created_at")
                    val createdAt: LocalDateTime, // 2022-05-04T09:51:05.000000Z
                    @SerializedName("updated_at")
                    val updatedAt: LocalDateTime, // 2022-05-15T15:04:43.000000Z
                )
            }
        }
    }
}


fun RecipeResponse.Data.toRecipe() = Recipe(
    type = parseRecipeType(),
    recipeId = id,
    name = attributes.name,
    foodType = foodType,
    price = attributes.price,
    available = attributes.available,
    food = attributes.food.map {
        Recipe.RecipeFood(it.id, it.attributes.name, it.attributes.quantity, it.attributes.units)
    })

fun RecipeResponse.Data.parseRecipeType() = when (attributes.type) {
    0 -> Recipe.RecipeType.ENTRANTE
    1 -> Recipe.RecipeType.FIRST_PLATE
    2 -> Recipe.RecipeType.SECOND_PLATE
    3 -> Recipe.RecipeType.DESERT
    4 -> Recipe.RecipeType.DRINK
    5 -> Recipe.RecipeType.COMPLEMENTS
    else -> Recipe.RecipeType.NONE
}


fun RecipeResponse.toRecipe() = data.toRecipe()