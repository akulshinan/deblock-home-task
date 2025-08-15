package org.deblock.exercise.domain

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.OffsetDateTime

@JvmInline
value class IataCode(val value: String) {
    init {
        require(value.matches(Regex("[A-Z]{3}"))) {
            "IATA code must be 3 uppercase letters"
        }
    }
}

@JvmInline
value class Airline(val value: String)

@JvmInline
value class SupplierName(val value: String)

@JvmInline
value class PassengerCount(val value: Int) {
    init {
        require(value in 1..4) { "Passengers must be between 1 and 4" }
    }
}

@JvmInline
value class Money(val amount: BigDecimal) {
    fun rounded2() = Money(amount.setScale(2, RoundingMode.HALF_UP))
    operator fun times(multiplier: Int) = Money(amount.multiply(BigDecimal(multiplier)))
}

data class FlightOffer(
    val airline: Airline,
    val supplier: SupplierName,
    val fare: Money,
    val departureAirportCode: IataCode,
    val destinationAirportCode: IataCode,
    val departureDate: OffsetDateTime,
    val arrivalDate: OffsetDateTime,
)
