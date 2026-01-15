package com.virakumaro.testbookcabin.domain.usecase

import com.virakumaro.testbookcabin.core.Results
import com.virakumaro.testbookcabin.domain.model.Booking
import com.virakumaro.testbookcabin.domain.repository.CheckInRepository
import kotlinx.coroutines.flow.Flow

class UpdatePassengerDetailsUseCase(private val repository: CheckInRepository) {
    operator fun invoke(
        booking: Booking,
        passport: String, firstName: String, lastName: String, gender: String,
        contactName: String, contactNumber: String, relationship: String
    ): Flow<Results<Unit>> {
        return repository.updatePassengerDetails(
            booking, passport, firstName, lastName, gender, contactName, contactNumber, relationship
        )
    }
}