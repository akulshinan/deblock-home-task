package org.deblock.exercise.infrastructure.api

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.OffsetDateTime

data class FlightOfferResponseDto(
    val airline: String,
    val supplier: String,
    val fare: String,
    val departureAirportCode: String,
    val destinationAirportCode: String,
    @param:JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    val departureDate: OffsetDateTime,
    @param:JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    val arrivalDate: OffsetDateTime,
)
