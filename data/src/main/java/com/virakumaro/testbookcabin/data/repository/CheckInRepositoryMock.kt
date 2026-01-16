package com.virakumaro.testbookcabin.data.repository

import android.content.Context
import com.google.gson.Gson
import com.virakumaro.testbookcabin.core.Results
import com.virakumaro.testbookcabin.core.helper.Helper.loadJsonFromAssets
import com.virakumaro.testbookcabin.data.dto.checkin.CheckInResponse
import com.virakumaro.testbookcabin.data.dto.detail.PassengerDetailsResponse
import com.virakumaro.testbookcabin.data.dto.document.PassengerDocumentsResponse
import com.virakumaro.testbookcabin.domain.model.BoardingPass
import com.virakumaro.testbookcabin.domain.model.Booking
import com.virakumaro.testbookcabin.domain.repository.CheckInRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CheckInRepositoryMock(
    private val context: Context
) : CheckInRepository {

    override fun getBooking(pnr: String, lastName: String): Flow<Results<Booking>> = flow {
        emit(Results.Loading)
        try {
            val jsonString = loadJsonFromAssets(context, "Response_PaxDetails.json")
            if (jsonString == null) {
                emit(Results.Error("Failed to load mock data from assets"))
                return@flow
            }
            val response = Gson().fromJson(jsonString, PassengerDetailsResponse::class.java)
            delay(1000)

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
            val jsonString = loadJsonFromAssets(context, "Response_PaxDocuments.json")
            if (jsonString == null) {
                emit(Results.Error("Failed to load mock data from assets"))
                return@flow
            }
            val response = Gson().fromJson(jsonString, PassengerDocumentsResponse::class.java)
            delay(1500)

            val isSuccess = response.results?.any { it.status?.firstOrNull()?.type == "SUCCESS" } ?: false
            if (isSuccess) emit(Results.Success(Unit))
            else emit(Results.Error("Failed to update documents based on mock data"))
        } catch (e: Exception) {
            emit(Results.Error(e.message ?: "Unknown error occurred during update"))
        }
    }

    override fun checkIn(passengerId: String): Flow<Results<BoardingPass>> = flow {
        emit(Results.Loading)
        try {
            val jsonString = loadJsonFromAssets(context, "Response_CheckIn.json")
            if (jsonString == null) {
                emit(Results.Error("Failed to load mock data from assets"))
                return@flow
            }
            val response = Gson().fromJson(jsonString, CheckInResponse::class.java)
            delay(1500)

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
