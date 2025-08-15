package org.deblock.exercise.application

import java.time.LocalDate
import org.deblock.exercise.domain.IataCode
import org.deblock.exercise.domain.PassengerCount

data class FlightSearchRequest(
    val origin: IataCode,
    val destination: IataCode,
    val departureDate: LocalDate,
    val returnDate: LocalDate,
    val passengers: PassengerCount,
)
