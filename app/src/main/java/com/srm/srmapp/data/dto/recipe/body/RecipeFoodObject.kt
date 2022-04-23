package com.srm.srmapp.data.dto.recipe.body

import androidx.annotation.Keep

@Keep
data class RecipeFoodObject(
    val food_id: Int,
    val quantity: Float,
)
