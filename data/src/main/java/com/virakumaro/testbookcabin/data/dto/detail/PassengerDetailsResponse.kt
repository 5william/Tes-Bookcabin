package com.virakumaro.testbookcabin.data.dto.detail

import com.google.gson.annotations.SerializedName

data class PassengerDetailsResponse(
    @SerializedName("reservation")
    val reservation: ReservationDto?
)

data class ReservationDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("passengers")
    val passengers: PassengersWrapper?,
    @SerializedName("itinerary")
    val itinerary: ItineraryWrapper?,
    @SerializedName("recordLocator")
    val recordLocator: String?
)

data class PassengersWrapper(
    @SerializedName("passenger")
    val passenger: List<PassengerDto>?
)

data class PassengerDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("personName")
    val personName: PersonNameDto?,
    @SerializedName("passengerDocument")
    val documents: List<DocumentWrapperDto>?,
    @SerializedName("emergencyContact")
    val emergencyContacts: List<EmergencyContactDto>?,
    @SerializedName("weightCategory")
    val weightCategory: String,
)

data class PersonNameDto(
    @SerializedName("first")
    val first: String?,
    @SerializedName("last")
    val last: String?
)

data class ItineraryWrapper(
    @SerializedName("itineraryPart")
    val itineraryPart: List<ItineraryPartDto>?
)

data class ItineraryPartDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("segment")
    val segment: List<SegmentDto>?
)

data class SegmentDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("flightDetail")
    val flightDetail: List<FlightDetailDto>?
)

data class FlightDetailDto(
    @SerializedName("flightNumber")
    val flightNumber: String?,
    @SerializedName("airline")
    val airline: String?,
    @SerializedName("departureAirport")
    val departureAirport: String?,
    @SerializedName("arrivalAirport")
    val arrivalAirport: String?,
    @SerializedName("departureTime")
    val departureTime: String?,
    @SerializedName("operatingAirlineName")
    val operatingAirlineName: String?,
    @SerializedName("departureGate")
    val departureGate: String?,
)

data class DocumentWrapperDto(
    @SerializedName("document")
    val document: DocumentInfoDto
)

data class DocumentInfoDto(
    @SerializedName("number")
    val number: String,
    @SerializedName("personName")
    val personName: PersonNameDto,
    @SerializedName("nationality")
    val nationality: String,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String,
    @SerializedName("expiryDate")
    val expiryDate: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("id")
    val id: String
)

data class EmergencyContactDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("contactDetails")
    val contactDetails: String,
    @SerializedName("relationship")
    val relationship: String,
    @SerializedName("id")
    val id: String
)