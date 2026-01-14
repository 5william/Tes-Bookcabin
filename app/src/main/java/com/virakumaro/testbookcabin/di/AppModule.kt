package com.virakumaro.testbookcabin.di

import com.virakumaro.testbookcabin.presentation.checkin.CheckInViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { CheckInViewModel(get()) }
}