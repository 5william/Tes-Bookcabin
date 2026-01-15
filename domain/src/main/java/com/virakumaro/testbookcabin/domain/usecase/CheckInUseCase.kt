package com.virakumaro.testbookcabin.domain.usecase

import com.virakumaro.testbookcabin.core.Results
import com.virakumaro.testbookcabin.domain.model.BoardingPass
import com.virakumaro.testbookcabin.domain.repository.CheckInRepository
import kotlinx.coroutines.flow.Flow

class CheckInUseCase(private val repository: CheckInRepository) {
    operator fun invoke(passengerId: String): Flow<Results<BoardingPass>> {
        return repository.checkIn(passengerId)
    }
}