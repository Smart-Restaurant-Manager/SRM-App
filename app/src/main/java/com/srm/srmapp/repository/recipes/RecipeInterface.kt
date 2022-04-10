package com.srm.srmapp.repository.recipes

import com.srm.srmapp.Resource
import com.srm.srmapp.data.dto.recipe.body.RecipeObject
import com.srm.srmapp.data.dto.recipe.response.RecipeListResponse
import com.srm.srmapp.data.dto.recipe.response.RecipeResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RecipeInterface {
    //No se implementa de momento
    @GET("/img/{img_url}")
    fun getRecipeImage(@Path("img_url") img_id: String): Call<Resource<Response<Unit>>>


    @GET("api/fake/recipes/")
    suspend fun getRecipes(): Response<RecipeListResponse>

    @GET("api/fake/recipes/{recipe}")
    suspend fun getRecipe(@Path("recipe") id: Int): Response<RecipeResponse>

    @POST("api/fake/recipes")
    suspend fun postRecipe(@Body postRecipe: RecipeObject): Response<RecipeResponse>

    @PUT("api/fake/recipes/{recipe}")
    suspend fun modifyRecipe(@Path("recipe") id: Int, @Body modRecipe: RecipeObject): Response<RecipeResponse>

}