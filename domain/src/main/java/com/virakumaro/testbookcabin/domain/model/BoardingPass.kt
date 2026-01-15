package com.virakumaro.testbookcabin.domain.model

data class BoardingPass(
    val passengerName: String,
    val flightNumber: String,
    val seatNumber: String,
    val origin: String,
    val destination: String,
    val boardingTime: String,
    val terminal: String,
    val gate: String,
    val barcodeString: String
)