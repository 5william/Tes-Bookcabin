package com.virakumaro.testbookcabin.data.dto.document

import com.google.gson.annotations.SerializedName
import com.virakumaro.testbookcabin.data.dto.detail.DocumentWrapperDto
import com.virakumaro.testbookcabin.data.dto.detail.EmergencyContactDto

data class PassengerDocumentsRequest(
    @SerializedName("returnSession")
    val returnSession: Boolean = false,
    @SerializedName("passengerDetails")
    val passengerDetails: List<PassengerDocDetailDto>
)

data class PassengerDocDetailDto(
    @SerializedName("documents")
    val documents: List<DocumentWrapperDto>,
    @SerializedName("emergencyContacts")
    val emergencyContacts: List<EmergencyContactDto>,
    @SerializedName("weightCategory")
    val weightCategory: String,
    @SerializedName("passengerId")
    val passengerId: String
)

