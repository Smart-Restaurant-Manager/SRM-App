package com.srm.srmapp.repository.authentication

import com.srm.srmapp.data.dto.auth.body.LoginObject
import com.srm.srmapp.data.dto.auth.body.SignupObject
import com.srm.srmapp.data.dto.auth.response.LoginResponse
import com.srm.srmapp.data.dto.auth.response.LogoutResponse
import com.srm.srmapp.data.dto.auth.response.SignupResponse
import com.srm.srmapp.data.dto.auth.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthInterface {
    @POST("/api/login")
    suspend fun login(@Body loginObject: LoginObject): Response<LoginResponse>

    @POST("/api/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<LogoutResponse>

    @POST("/api/register")
    suspend fun signup(@Body signupObject: SignupObject): Response<SignupResponse>

    @GET("/api/v1/user")
    suspend fun getUser(@Header("Authorization") token: String): Response<UserResponse>
}