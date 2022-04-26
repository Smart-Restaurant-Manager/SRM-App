package com.srm.srmapp.data.dto.recipe.body

import androidx.annotation.Keep
import com.srm.srmapp.data.models.Recipe

@Keep
data class RecipeObject(
    val name: String,
    val price: Float,
    val available: Boolean? = null,
    val food: List<RecipeFoodObject>? = null,
    val type: Int
)
