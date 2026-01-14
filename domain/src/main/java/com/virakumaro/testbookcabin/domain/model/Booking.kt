package com.virakumaro.testbookcabin.domain.model

data class Booking(
    val pnr: String,
    val firstName: String,
    val lastName: String,
    val flightNumber: String,
    val departureCity: String,
    val arrivalCity: String,
    val departureTime: String,
)