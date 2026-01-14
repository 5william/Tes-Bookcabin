package com.virakumaro.testbookcabin.data.dto

data class PassengerDetailsResponse(
    val reservation: ReservationDto?
)

data class ReservationDto(
    val id: String?,
    val passengers: PassengersWrapper?,
    val itinerary: ItineraryWrapper?
)

data class PassengersWrapper(
    val passenger: List<PassengerDto>?
)

data class PassengerDto(
    val id: String?,
    val personName: PersonNameDto?
)

data class PersonNameDto(
    val first: String?,
    val last: String?
)

data class ItineraryWrapper(
    val itineraryPart: List<ItineraryPartDto>?
)

data class ItineraryPartDto(
    val id: String?,
    val segment: List<SegmentDto>?
)

data class SegmentDto(
    val id: String?,
    val flightDetail: List<FlightDetailDto>?
)

data class FlightDetailDto(
    val flightNumber: String?,
    val airline: String?,
    val departureAirport: String?,
    val arrivalAirport: String?,
    val departureTime: String?,
    val operatingAirlineName: String?
)