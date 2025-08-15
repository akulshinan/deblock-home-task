package org.deblock.exercise.infrastructure.supplier.toughjet

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneOffset
import org.deblock.exercise.application.FlightSearchRequest
import org.deblock.exercise.application.port.SupplierPort
import org.deblock.exercise.domain.Airline
import org.deblock.exercise.domain.FlightOffer
import org.deblock.exercise.domain.IataCode
import org.deblock.exercise.domain.Money
import org.deblock.exercise.domain.SupplierName

@Component
class ToughJetSupplier(
    private val api: ToughJetApi,
    circuitBreakerRegistry: CircuitBreakerRegistry,
): SupplierPort {
    override val name: String = "ToughJet"
    private val cb = circuitBreakerRegistry.circuitBreaker("toughJet")

    override suspend fun getOffers(params: FlightSearchRequest): List<FlightOffer> =
        cb.executeSuspendFunction {
            api.search(
                from = params.origin.value,
                to = params.destination.value,
                outboundDate = params.departureDate.toString(),
                inboundDate = params.returnDate.toString(),
                numberOfAdults = params.passengers.value
            ).map { it.toDomain(params) }
        }

    private fun ToughJetResponse.toDomain(params: FlightSearchRequest): FlightOffer {
        val base = BigDecimal.valueOf(basePrice)
        val taxBD = BigDecimal.valueOf(tax)
        val discountMultiplier = BigDecimal.ONE - BigDecimal.valueOf(discount)
            .divide(BigDecimal.valueOf(100))
        val unitPrice = Money((base + taxBD) * discountMultiplier)
        val total = (unitPrice * params.passengers.value).rounded2()
        return FlightOffer(
            airline = Airline(carrier),
            supplier = SupplierName(name),
            fare = total,
            departureAirportCode = IataCode(departureAirportName),
            destinationAirportCode = IataCode(arrivalAirportName),
            departureDate = Instant.parse(outboundDateTime).atOffset(ZoneOffset.UTC), // here better to use customer ZoneOffset
            arrivalDate = Instant.parse(inboundDateTime).atOffset(ZoneOffset.UTC), // here better to use customer ZoneOffset
        )
    }
}
