package com.virakumaro.testbookcabin.presentation.onlinecheckin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virakumaro.testbookcabin.core.Results
import com.virakumaro.testbookcabin.domain.model.Booking
import com.virakumaro.testbookcabin.domain.usecase.GetBookingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnlineCheckInViewModel(
    private val getBookingUseCase: GetBookingUseCase
) : ViewModel() {

    private val _bookingState = MutableStateFlow<Results<Booking>?>(null)
    val bookingState: StateFlow<Results<Booking>?> = _bookingState.asStateFlow()

    var selectedBooking by mutableStateOf<Booking?>(null)
    var pnr by mutableStateOf("SDQASDA")
    var lastName by mutableStateOf("Test")

    fun findBooking() {
        viewModelScope.launch {
            getBookingUseCase(pnr, lastName).collect { result ->
                _bookingState.value = result
                if (result is Results.Success) {
                    selectedBooking = result.data
                }
            }
        }
    }

    fun setStateIdle() {
        _bookingState.value = null
    }
}