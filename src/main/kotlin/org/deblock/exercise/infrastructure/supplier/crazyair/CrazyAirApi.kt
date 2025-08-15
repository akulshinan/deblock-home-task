package org.deblock.exercise.infrastructure.supplier.crazyair

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime
import retrofit2.http.GET
import retrofit2.http.Query

interface CrazyAirApi {
    @GET("/crazyair/flights")
    suspend fun search(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("departureDate") departureDate: String,
        @Query("returnDate") returnDate: String,
        @Query("passengerCount") passengerCount: Int,
    ): List<CrazyAirResponse>
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class CrazyAirResponse(
    val airline: String,
    val price: Double,
    val cabinclass: String,
    val departureAirportCode: String,
    val destinationAirportCode: String,
    val departureDate: LocalDateTime,
    val arrivalDate: LocalDateTime,
)
