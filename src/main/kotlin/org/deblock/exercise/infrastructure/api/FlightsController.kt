package org.deblock.exercise.infrastructure.api

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import java.time.LocalDate
import org.deblock.exercise.application.ports.FlightsSearchUseCase
import org.deblock.exercise.application.ports.Found
import org.deblock.exercise.application.ports.NotFound
import org.deblock.exercise.domain.FlightOffer
import org.deblock.exercise.domain.FlightSearchRequest
import org.deblock.exercise.domain.IataCode
import org.deblock.exercise.domain.PassengerCount
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/flights")
class FlightsController(
    private val useCase: FlightsSearchUseCase
) {
    @GetMapping
    suspend fun search(
        @RequestParam @Pattern(regexp = "[A-Z]{3}") origin: String,
        @RequestParam @Pattern(regexp = "[A-Z]{3}") destination: String,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) departureDate: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) returnDate: LocalDate,
        @RequestParam("numberOfPassengers") @Min(1) @Max(4) numberOfPassengers: Int,
    ): ResponseEntity<Any> {

        val request = FlightSearchRequest(
            origin = IataCode(origin),
            destination = IataCode(destination),
            departureDate = departureDate,
            returnDate = returnDate,
            passengers = PassengerCount(numberOfPassengers),
        )

        return when (val result = useCase.search(request)) {
            is Found -> ResponseEntity.ok()
                .body(result.offers.map { it.toDto() })

            is NotFound -> ResponseEntity.notFound().build()
        }
    }
}

private fun FlightOffer.toDto() = FlightOfferResponse(
    airline = airline.value,
    supplier = supplier.value,
    fare = fare.rounded2().amount.toString(),
    departureAirportCode = departureAirportCode.value,
    destinationAirportCode = destinationAirportCode.value,
    departureDate = departureDate,
    arrivalDate = arrivalDate
)
