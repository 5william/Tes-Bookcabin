package com.virakumaro.testbookcabin.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.virakumaro.testbookcabin.core.Results
import com.virakumaro.testbookcabin.core.helper.Helper.loadJsonFromAssets
import com.virakumaro.testbookcabin.data.api.CheckInApi
import com.virakumaro.testbookcabin.data.dto.PassengerDetailsRequest
import com.virakumaro.testbookcabin.data.dto.PassengerDetailsResponse
import com.virakumaro.testbookcabin.data.dto.ReservationCriteria
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
                val request = PassengerDetailsRequest(
                    reservationCriteria = ReservationCriteria(pnr, lastName)
                )
                response = checkInApi.getPassengerDetails(request = request)
            }


            val passenger = response.reservation?.passengers?.passenger?.firstOrNull()
            val flightDetail = response.reservation?.itinerary?.itineraryPart?.firstOrNull()
                ?.segment?.firstOrNull()
                ?.flightDetail?.firstOrNull()

            if (passenger != null) {
                val booking = Booking(
                    pnr = pnr,
                    firstName = passenger.personName?.first ?: "",
                    lastName = passenger.personName?.last ?: "",

                    flightNumber = "${flightDetail?.airline} ${flightDetail?.flightNumber}",

                    departureCity = flightDetail?.departureAirport ?: "",
                    arrivalCity = flightDetail?.arrivalAirport ?: "",
                    departureTime = flightDetail?.departureTime ?: ""
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
}