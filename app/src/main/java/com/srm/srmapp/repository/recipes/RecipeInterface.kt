package com.srm.srmapp.repository.recipes

import com.srm.srmapp.data.dto.recipe.body.RecipeObject
import com.srm.srmapp.data.dto.recipe.response.RecipeListResponse
import com.srm.srmapp.data.dto.recipe.response.RecipeResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RecipeInterface {
    @GET("/img/{img_url}")
    @Streaming
    suspend fun getRecipeImage(@Path("img_url") img_id: String): ResponseBody

    @GET("api/v1/recipes/")
    suspend fun getRecipes(): Response<RecipeListResponse>

    @GET("api/v1/recipes/{recipe}")
    suspend fun getRecipe(@Path("recipe") id: Int): Response<RecipeResponse>

    @POST("api/v1/recipes")
    suspend fun postRecipe(@Body postRecipe: RecipeObject): Response<Unit>

    @PUT("api/v1/recipes/{recipe}")
    suspend fun modifyRecipe(@Path("recipe") id: Int, @Body modRecipe: RecipeObject): Response<Unit>
}