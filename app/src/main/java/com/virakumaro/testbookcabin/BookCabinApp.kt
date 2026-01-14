package com.virakumaro.testbookcabin

import android.app.Application
import com.virakumaro.testbookcabin.data.di.dataModule
import com.virakumaro.testbookcabin.di.appModule
import com.virakumaro.testbookcabin.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class BookCabinApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BookCabinApp)
            modules(listOf(domainModule, dataModule, appModule))
        }
    }
}