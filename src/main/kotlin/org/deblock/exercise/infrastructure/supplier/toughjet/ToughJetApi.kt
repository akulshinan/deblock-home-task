package org.deblock.exercise.infrastructure.supplier.toughjet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import retrofit2.http.GET
import retrofit2.http.Query

interface ToughJetApi {
    @GET("/toughjet/flights")
    suspend fun search(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("outboundDate") outboundDate: String, // ISO_LOCAL_DATE
        @Query("inboundDate") inboundDate: String,   // ISO_LOCAL_DATE
        @Query("numberOfAdults") numberOfAdults: Int,
    ): List<ToughJetResponse>
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class ToughJetResponse(
    val carrier: String,
    val basePrice: Double,
    val tax: Double,
    val discount: Double,
    val departureAirportName: String,
    val arrivalAirportName: String,
    val outboundDateTime: String,
    val inboundDateTime: String,
)
