package com.virakumaro.testbookcabin.data.di

import com.virakumaro.testbookcabin.data.BuildConfig
import com.virakumaro.testbookcabin.data.api.AuthApi
import com.virakumaro.testbookcabin.data.api.CheckInApi
import com.virakumaro.testbookcabin.data.interceptor.AuthInterceptor
import com.virakumaro.testbookcabin.data.interceptor.SessionInterceptor
import com.virakumaro.testbookcabin.data.local.TokenManager
import com.virakumaro.testbookcabin.data.repository.CheckInRepositoryImpl
import com.virakumaro.testbookcabin.domain.repository.CheckInRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {
    single { TokenManager(get()) }
    single { SessionInterceptor(get()) }
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    single(named("AuthRetrofit")) {
        val client = OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single {
        get<Retrofit>(named("AuthRetrofit")).create(AuthApi::class.java)
    }
    single { AuthInterceptor(get(), get()) }

    single(named("MainRetrofit")) {
        val client = OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(get<SessionInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single {
        get<Retrofit>(named("MainRetrofit")).create(CheckInApi::class.java)
    }
    single<CheckInRepository> { CheckInRepositoryImpl(get(), get()) }

}