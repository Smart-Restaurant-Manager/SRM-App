package com.srm.srmapp.repository.recipes

import com.srm.srmapp.Resource
import com.srm.srmapp.data.dto.recipe.body.RecipeObject
import com.srm.srmapp.repository.BaseRepository
import javax.inject.Inject

class RecipeRepository @Inject constructor(private val api: RecipeInterface) : BaseRepository() {
    suspend fun getRecipes() = safeApiCall({
        api.getRecipes()
    }) {
        it.toRecipeList()
    }

    suspend fun getRecipe(id: Int) = safeApiCall({
        api.getRecipe(id)
    }) {
        it.toRecipe()
    }

    suspend fun postRecipe(name: String, price: Float, available: Boolean, food: Array<Int>): Resource<String> {
        return safeApiCall({
            api.postRecipe(RecipeObject(name, price, available, null))
        }) {
            "Recipe added"
        }
    }

    suspend fun modRecipe(id: Int, name: String, price: Float, available: Boolean, food: Array<Int>): Resource<String> {
        return safeApiCall({
            api.putRecipe(id, RecipeObject(name, price, available, null))
        }) {
            "Recipe Modified"
        }
    }
}

