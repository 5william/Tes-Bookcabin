package com.virakumaro.testbookcabin.data.dto.checkin

import com.google.gson.annotations.SerializedName

data class CheckInRequest(
    @SerializedName("returnSession")
    val returnSession: Boolean = false,

    @SerializedName("passengerIds")
    val passengerIds: List<String>,

    @SerializedName("outputFormat")
    val outputFormat: String = "BPXML",

    @SerializedName("waiveAutoReturnCheckIn")
    val waiveAutoReturnCheckIn: Boolean = false
)