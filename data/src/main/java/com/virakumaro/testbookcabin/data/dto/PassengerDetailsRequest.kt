package com.virakumaro.testbookcabin.data.dto

data class PassengerDetailsRequest(
    val reservationCriteria: ReservationCriteria,
    val outputFormat: String = "BPXML"
)
data class ReservationCriteria(
    val recordLocator: String,
    val lastName: String
)