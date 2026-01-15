package com.virakumaro.testbookcabin.domain.repository

import com.virakumaro.testbookcabin.core.Results
import com.virakumaro.testbookcabin.domain.model.BoardingPass
import com.virakumaro.testbookcabin.domain.model.Booking
import kotlinx.coroutines.flow.Flow

interface CheckInRepository {

    fun getBooking(pnr: String, lastName: String): Flow<Results<Booking>>

    fun updatePassengerDetails(
        booking: Booking,
        passport: String,
        firstName: String,
        lastName: String,
        gender: String,
        contactName: String,
        contactNumber: String,
        relationship: String
    ): Flow<Results<Unit>>

    fun checkIn(passengerId: String): Flow<Results<BoardingPass>>
}