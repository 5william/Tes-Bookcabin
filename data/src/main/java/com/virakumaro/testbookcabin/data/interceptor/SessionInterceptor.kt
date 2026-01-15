package com.virakumaro.testbookcabin.data.interceptor

import android.util.Log
import com.virakumaro.testbookcabin.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class SessionInterceptor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        tokenManager.getSessionId()?.let { id ->
            requestBuilder.addHeader("Session-ID", id)
            Log.d("SessionInterceptor", "Attaching Session-ID: $id")
        }

        val response = chain.proceed(requestBuilder.build())

        val newSessionId = response.header("Session-ID")

        if (!newSessionId.isNullOrEmpty()) {
            val currentSessionId = tokenManager.getSessionId()
            if (currentSessionId != newSessionId) {
                tokenManager.saveSessionId(newSessionId)
                Log.d("SessionInterceptor", "New Session-ID saved: $newSessionId")
            }
        }

        return response
    }
}