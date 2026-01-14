package com.virakumaro.testbookcabin.data.api

import com.virakumaro.testbookcabin.data.dto.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST


interface AuthApi {
    @FormUrlEncoded
    @POST("v2/auth/token")
    suspend fun getAuthToken(
        @Header("Authorization") authHeader: String,
        @Field("grant_type") grantType: String = "client_credentials"
    ): TokenResponse
}