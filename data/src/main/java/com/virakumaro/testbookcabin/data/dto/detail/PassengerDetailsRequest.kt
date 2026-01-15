package com.virakumaro.testbookcabin.data.dto.detail

import com.google.gson.annotations.SerializedName

data class PassengerDetailsRequest(
    @SerializedName("reservationCriteria")
    val reservationCriteria: ReservationCriteria,
    @SerializedName("outputFormat")
    val outputFormat: String = "BPXML"
)
data class ReservationCriteria(
    @SerializedName("recordLocator")
    val recordLocator: String,
    @SerializedName("lastName")
    val lastName: String
)