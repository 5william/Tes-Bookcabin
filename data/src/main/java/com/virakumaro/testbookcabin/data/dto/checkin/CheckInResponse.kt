package com.virakumaro.testbookcabin.data.dto.checkin

import com.google.gson.annotations.SerializedName
import com.virakumaro.testbookcabin.data.dto.detail.FlightDetailDto
import com.virakumaro.testbookcabin.data.dto.detail.PersonNameDto
import com.virakumaro.testbookcabin.data.dto.document.StatusDto

data class CheckInResponse(
    @SerializedName("boardingPasses")
    val boardingPasses: List<BoardingPassWrapperDto>?,
    @SerializedName("results")
    val results: List<CheckInResultDto>?
)

data class CheckInResultDto(
    @SerializedName("status")
    val status: List<StatusDto>?
)

data class BoardingPassWrapperDto(
    @SerializedName("passengerFlightId")
    val passengerFlightId: String?,
    @SerializedName("boardingPass")
    val boardingPass: BoardingPassDto?
)

data class BoardingPassDto(
    @SerializedName("barCode")
    val barCode: String?,
    @SerializedName("seat")
    val seat: SeatDto?,
    @SerializedName("flightDetail")
    val flightDetail: FlightDetailDto?,
    @SerializedName("personName")
    val personName: PersonNameDto?
)

data class SeatDto(
    @SerializedName("value")
    val number: String?
)