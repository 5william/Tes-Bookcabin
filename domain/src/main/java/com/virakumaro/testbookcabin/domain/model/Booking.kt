package com.virakumaro.testbookcabin.domain.model

data class Booking(
    val pnr: String,
    val firstName: String,
    val lastName: String,
    val passengerId: String,
    val documentId: String,
    val documentType: String,
    val emergencyContactId: String,
    val dateOfBirth: String,
    val expiryDate: String,
    val nationality: String,
    val gender: String,
    val weightCategory: String,
    val flightNumber: String,
    val departureCity: String,
    val arrivalCity: String,
    val departureTime: String,
)