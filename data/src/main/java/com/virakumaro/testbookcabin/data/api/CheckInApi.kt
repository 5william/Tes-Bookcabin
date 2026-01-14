package com.virakumaro.testbookcabin.data.api

import com.virakumaro.testbookcabin.data.dto.PassengerDetailsRequest
import com.virakumaro.testbookcabin.data.dto.PassengerDetailsResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CheckInApi {
    @POST("v918/dcci/passenger/details")
    suspend fun getPassengerDetails(
        @Query("jipcc") jipcc: String = "ODCI",
        @Body request: PassengerDetailsRequest
    ): PassengerDetailsResponse
}