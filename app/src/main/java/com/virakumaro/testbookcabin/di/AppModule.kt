package com.virakumaro.testbookcabin.di

import com.virakumaro.testbookcabin.presentation.checkin.CheckInSummaryViewModel
import com.virakumaro.testbookcabin.presentation.detail.PassengerDetailsViewModel
import com.virakumaro.testbookcabin.presentation.onlinecheckin.OnlineCheckInViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { OnlineCheckInViewModel(get()) }
    viewModel { PassengerDetailsViewModel(get()) }
    viewModel { CheckInSummaryViewModel(get()) }
}