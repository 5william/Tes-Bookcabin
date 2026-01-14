package com.virakumaro.testbookcabin.presentation.checkin

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

class CheckInViewModel(
    private val getBookingUseCase: GetBookingUseCase
) : ViewModel() {

    private val _bookingState = MutableStateFlow<Results<Booking>?>(null)
    val bookingState: StateFlow<Results<Booking>?> = _bookingState.asStateFlow()

    var pnr by mutableStateOf("")
    var lastName by mutableStateOf("")

    fun findBooking() {
        viewModelScope.launch {
            getBookingUseCase(pnr, lastName).collect { result ->
                _bookingState.value = result
            }
        }
    }

    fun resetState() {
        _bookingState.value = null
    }
}