package com.virakumaro.testbookcabin.data.api

import com.virakumaro.testbookcabin.data.dto.detail.PassengerDetailsRequest
import com.virakumaro.testbookcabin.data.dto.detail.PassengerDetailsResponse
import com.virakumaro.testbookcabin.data.dto.document.PassengerDocumentsRequest
import com.virakumaro.testbookcabin.data.dto.document.PassengerDocumentsResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface CheckInApi {
    @POST("v918/dcci/passenger/details")
    suspend fun getPassengerDetails(
        @Query("jipcc") jipcc: String = "ODCI",
        @Body request: PassengerDetailsRequest
    ): PassengerDetailsResponse

    @POST("v918/dcci/passenger/details")
    suspend fun updatePassengerDetails(
        @Query("jipcc") jipcc: String = "ODCI",
        @Body request: PassengerDocumentsRequest
    ): PassengerDocumentsResponse

}