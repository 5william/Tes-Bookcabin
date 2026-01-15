package com.virakumaro.testbookcabin.presentation.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virakumaro.testbookcabin.core.Results
import com.virakumaro.testbookcabin.domain.model.BoardingPass
import com.virakumaro.testbookcabin.domain.usecase.CheckInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CheckInSummaryViewModel(
    private val checkInUseCase: CheckInUseCase
) : ViewModel() {

    private val _checkInState = MutableStateFlow<Results<BoardingPass>?>(null)
    val checkInState = _checkInState.asStateFlow()

    var boardingPassData: BoardingPass? = null
        private set

    fun performCheckIn(passengerId: String) {
        viewModelScope.launch {
            checkInUseCase(passengerId).collect { result ->
                _checkInState.value = result
                if (result is Results.Success) {
                    boardingPassData = result.data
                }
            }
        }
    }

    fun setStateIdle() {
        _checkInState.value = null
    }
}