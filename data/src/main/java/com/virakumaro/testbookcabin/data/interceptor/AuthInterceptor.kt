package com.virakumaro.testbookcabin.data.interceptor

import com.virakumaro.testbookcabin.data.BuildConfig
import com.virakumaro.testbookcabin.data.local.TokenManager
import com.virakumaro.testbookcabin.data.api.AuthApi
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenManager: TokenManager,
    private val authApi: AuthApi
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val currentToken = tokenManager.getToken()

        val requestBuilder = originalRequest.newBuilder()
        if (currentToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer $currentToken")
        }

        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401) {
            response.close()

            synchronized(this) {
                val newToken = tokenManager.getToken()

                if (newToken == currentToken || newToken == null) {
                    val freshToken = fetchNewToken()

                    if (freshToken != null) {
                        tokenManager.saveToken(freshToken)

                        val newRequest = originalRequest.newBuilder()
                            .addHeader("Authorization", "Bearer $freshToken")
                            .build()

                        return chain.proceed(newRequest)
                    }
                } else {
                    val newRequest = originalRequest.newBuilder()
                        .addHeader("Authorization", "Bearer $newToken")
                        .build()
                    return chain.proceed(newRequest)
                }
            }
        }

        return response
    }

    private fun fetchNewToken(): String? {
        return try {
            // Panggil API Auth menggunakan runBlocking karena Interceptor berjalan synchronous
            runBlocking {
                val response = authApi.getAuthToken(BuildConfig.AUTH_KEY)
                response.accessToken
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}