package com.srm.srmapp.repository.recipes

import com.srm.srmapp.data.dto.recipe.response.toRecipe
import com.srm.srmapp.data.dto.recipe.response.toRecipeList
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.repository.BaseRepository
import javax.inject.Inject

class RecipeRepository @Inject constructor(private val api: RecipeInterface) : BaseRepository() {
    suspend fun getRecipes() =
        safeApiCall({
            api.getRecipes()
        }) {
            it.toRecipeList()
        }

    suspend fun getRecipe(id: Int) =
        safeApiCall({
            api.getRecipe(id)
        }) {
            it.toRecipe()
        }

    suspend fun postRecipe(recipe: Recipe) =
        safeApiCall({
            api.postRecipe(recipe.toJsonObject())
        }) {
            "Recipe added"
        }

    suspend fun putRecipe(recipe: Recipe) =
        safeApiCall({
            api.putRecipe(recipe.id, recipe.toJsonObject())
        }) {
            "Recipe Modified"
        }

    suspend fun deleteRecipe(id: Int) =
        safeApiCall({
            api.deleteRecipe(id)
        }) {
            "Recipe deleted"
        }
}