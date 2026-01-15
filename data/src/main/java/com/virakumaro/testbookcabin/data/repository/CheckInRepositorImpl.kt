package com.virakumaro.testbookcabin.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.virakumaro.testbookcabin.core.Results
import com.virakumaro.testbookcabin.core.helper.Helper.loadJsonFromAssets
import com.virakumaro.testbookcabin.data.api.CheckInApi
import com.virakumaro.testbookcabin.data.dto.checkin.CheckInRequest
import com.virakumaro.testbookcabin.data.dto.checkin.CheckInResponse
import com.virakumaro.testbookcabin.data.dto.detail.DocumentInfoDto
import com.virakumaro.testbookcabin.data.dto.detail.DocumentWrapperDto
import com.virakumaro.testbookcabin.data.dto.detail.EmergencyContactDto
import com.virakumaro.testbookcabin.data.dto.detail.PassengerDetailsRequest
import com.virakumaro.testbookcabin.data.dto.detail.PassengerDetailsResponse
import com.virakumaro.testbookcabin.data.dto.detail.PersonNameDto
import com.virakumaro.testbookcabin.data.dto.detail.ReservationCriteria
import com.virakumaro.testbookcabin.data.dto.document.PassengerDocDetailDto
import com.virakumaro.testbookcabin.data.dto.document.PassengerDocumentsRequest
import com.virakumaro.testbookcabin.data.dto.document.PassengerDocumentsResponse
import com.virakumaro.testbookcabin.domain.model.BoardingPass
import com.virakumaro.testbookcabin.domain.model.Booking
import com.virakumaro.testbookcabin.domain.repository.CheckInRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException


class CheckInRepositoryImpl(
    private val checkInApi: CheckInApi,
    private val context: Context
) : CheckInRepository {

    private val USE_MOCK_DATA = true

    override fun getBooking(pnr: String, lastName: String): Flow<Results<Booking>> = flow {
        emit(Results.Loading)

        try {
            val response: PassengerDetailsResponse

            val request = PassengerDetailsRequest(
                reservationCriteria = ReservationCriteria(pnr, lastName)
            )
            if (USE_MOCK_DATA) {
                val jsonString = loadJsonFromAssets(context, "Response_PaxDetails.json")
                if (jsonString != null) {
                    response = Gson().fromJson(jsonString, PassengerDetailsResponse::class.java)
                } else {
                    emit(Results.Error("Failed to load mock data from assets"))
                    return@flow
                }
                delay(1000)
            } else {
                response = checkInApi.getPassengerDetails(request = request)
            }



            val passenger = response.reservation?.passengers?.passenger?.firstOrNull()
            val flightDetail = response.reservation?.itinerary?.itineraryPart?.firstOrNull()
                ?.segment?.firstOrNull()
                ?.flightDetail?.firstOrNull()

            if (passenger != null && flightDetail != null) {
                val existingDoc = passenger.documents?.firstOrNull()?.document
                val existingContact = passenger.emergencyContacts?.firstOrNull()
                val booking = Booking(
                    pnr = pnr,
                    passengerId = passenger.id ?: "",
                    firstName = passenger.personName?.first ?: "",
                    lastName = passenger.personName?.last ?: "",

                    documentId = existingDoc?.id ?: "",
                    documentType = existingDoc?.type ?: "",
                    emergencyContactId = existingContact?.id ?: "",
                    dateOfBirth = existingDoc?.dateOfBirth ?: "",
                    expiryDate = existingDoc?.expiryDate ?: "",
                    nationality = existingDoc?.nationality ?: "",
                    gender = existingDoc?.gender ?: "",

                    flightNumber = "${flightDetail.airline ?: ""} ${flightDetail.flightNumber ?: ""}".trim(),
                    departureCity = flightDetail.departureAirport ?: "",
                    arrivalCity = flightDetail.arrivalAirport ?: "",
                    departureTime = flightDetail.departureTime ?: "",
                    weightCategory = passenger.weightCategory
                )
                emit(Results.Success(booking))
            } else {
                emit(Results.Error("Passenger not found in reservation"))
            }

        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                if (!errorBody.isNullOrEmpty()) {
                    val gson = Gson()
                    val jsonObject = gson.fromJson(errorBody, JsonObject::class.java)

                    when {
                        jsonObject.has("message") -> jsonObject.get("message").asString
                        else -> "Server Error"
                    }
                } else {
                    "An error occurred (Code: ${e.code()})"
                }
            } catch (e: Exception) {
                "Failed to process server request."
            }
            emit(Results.Error(errorMessage))
        } catch (e: IOException) {
            emit(Results.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            emit(Results.Error(e.message ?: "Unknown Error"))
        }
    }

    override fun updatePassengerDetails(
        booking: Booking,
        passport: String,
        firstName: String,
        lastName: String,
        gender: String,
        contactName: String,
        contactNumber: String,
        relationship: String
    ): Flow<Results<Unit>> = flow {
        emit(Results.Loading)
        try {
            val request = PassengerDocumentsRequest(
                passengerDetails = listOf(
                    PassengerDocDetailDto(
                        passengerId = booking.passengerId,
                        documents = listOf(
                            DocumentWrapperDto(
                                DocumentInfoDto(
                                    number = passport,
                                    personName = PersonNameDto(firstName, lastName),
                                    nationality = booking.nationality,
                                    dateOfBirth = booking.dateOfBirth,
                                    expiryDate = booking.expiryDate,
                                    gender = gender,
                                    id = booking.documentId,
                                    type = booking.documentType
                                )
                            )
                        ),
                        emergencyContacts = listOf(
                            EmergencyContactDto(
                                name = contactName,
                                countryCode = booking.nationality,
                                contactDetails = contactNumber,
                                relationship = relationship,
                                id = booking.documentId
                            )
                        ),
                        weightCategory = booking.weightCategory
                    )
                )
            )
            if (USE_MOCK_DATA) {
                val jsonString = loadJsonFromAssets(context, "Response_PaxDocuments.json")
                val response = Gson().fromJson(jsonString, PassengerDocumentsResponse::class.java)

                delay(1500)

                val isSuccess = response.results?.any { it.status?.firstOrNull()?.type == "SUCCESS" } ?: false
                if (isSuccess) emit(Results.Success(Unit))
                else emit(Results.Error("Failed to update documents based on mock data"))
            } else {

                checkInApi.updatePassengerDetails(request = request)
                emit(Results.Success(Unit))
            }

        } catch (e: Exception) {
            emit(Results.Error(e.message ?: "Unknown error occurred during update"))
        }
    }


    override fun checkIn(passengerId: String): Flow<Results<BoardingPass>> = flow {
        emit(Results.Loading)
        try {

            val request = CheckInRequest(
                passengerIds = listOf(passengerId),
                returnSession = false,
                outputFormat = "BPXML",
                waiveAutoReturnCheckIn = false
            )

            val response: CheckInResponse
            if (USE_MOCK_DATA) {
                val jsonString = loadJsonFromAssets(context, "Response_CheckIn.json")
                response = Gson().fromJson(jsonString, CheckInResponse::class.java)
                delay(1500)
            } else {
                response = checkInApi.checkIn(request = request)
            }

            val bpDto = response.boardingPasses?.firstOrNull()?.boardingPass

            if (bpDto != null) {
                val flightDetail = bpDto.flightDetail
                val boardingPass = BoardingPass(
                    passengerName = "${bpDto.personName?.first} ${bpDto.personName?.last}",
                    flightNumber = "${flightDetail?.airline} ${flightDetail?.flightNumber}",
                    seatNumber = bpDto.seat?.number ?: "",
                    origin = flightDetail?.departureAirport ?: "",
                    destination = flightDetail?.arrivalAirport ?: "",
                    boardingTime = flightDetail?.departureTime ?: "",
                    terminal = flightDetail?.departureAirport ?: "",
                    gate = flightDetail?.departureGate ?: "",
                    barcodeString = bpDto.barCode ?: ""
                )
                emit(Results.Success(boardingPass))
            } else {
                emit(Results.Error("Failed to retrieve Boarding Pass"))
            }


        } catch (e: Exception) {
            emit(Results.Error(e.message ?: "Check-In Failed"))
        }
    }


}