package com.virakumaro.testbookcabin.domain.repository

import com.virakumaro.testbookcabin.core.Results
import com.virakumaro.testbookcabin.domain.model.Booking
import kotlinx.coroutines.flow.Flow

interface CheckInRepository {

    fun getBooking(pnr: String, lastName: String): Flow<Results<Booking>>
}