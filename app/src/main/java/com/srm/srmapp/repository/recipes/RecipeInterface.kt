package com.srm.srmapp.repository.recipes

import com.srm.srmapp.Resource
import com.srm.srmapp.data.dto.recipe.RecipeResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeInterface {
    @GET("/img/{img_url}")
    fun getRecipeImage(@Path("img_url") img_id: String): Call<Resource<Response<Unit>>>


    @GET("api/fake/recipes/{recipe}")
    fun getRecipe(@Path("recipe") id: Int): Resource<RecipeResponse>

}