package com.srm.srmapp.repository

import com.srm.srmapp.data.dao.UserResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @POST("/login")
    suspend fun login(@Query("username") username: String, @Query("password") password: String): Response<UserResponse>
}