package com.virakumaro.testbookcabin.data.dto.document

data class PassengerDocumentsResponse(
    val results: List<UpdateResultDto>?
)

data class UpdateResultDto(
    val status: List<StatusDto>?
)

data class StatusDto(
    val type: String?
)