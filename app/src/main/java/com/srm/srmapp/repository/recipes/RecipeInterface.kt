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

    @GET("api/v1/recipes")
    suspend fun getRecipes(): Response<RecipeListResponse>

    @POST("api/v1/recipes")
    suspend fun postRecipe(@Body postRecipe: RecipeObject): Response<Unit>

    @GET("api/v1/recipes/{recipe}")
    suspend fun getRecipe(@Path("recipe") id: Int): Response<RecipeResponse>

    @PUT("api/v1/recipes/{recipe}")
    suspend fun putRecipe(@Path("recipe") id: Int, @Body modRecipe: RecipeObject): Response<Unit>

    @DELETE("api/v1/recipes/{recipe}")
    suspend fun deleteRecipe(@Path("recipe") id: Int): Response<Unit>
}