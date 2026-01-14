package com.virakumaro.testbookcabin.domain.di

import com.virakumaro.testbookcabin.domain.usecase.GetBookingUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetBookingUseCase(get()) }
}