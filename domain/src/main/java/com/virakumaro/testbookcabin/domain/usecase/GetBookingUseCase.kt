package com.virakumaro.testbookcabin.domain.usecase

import com.virakumaro.testbookcabin.core.Results
import com.virakumaro.testbookcabin.domain.model.Booking
import com.virakumaro.testbookcabin.domain.repository.CheckInRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetBookingUseCase(private val repository: CheckInRepository) {
    operator fun invoke(pnr: String, lastName: String): Flow<Results<Booking>> {
        if (pnr.isBlank() || lastName.isBlank()) {
            return flow { emit(Results.Error("PNR and Last Name cannot be empty")) }
        }
        return repository.getBooking(pnr, lastName)
    }
}