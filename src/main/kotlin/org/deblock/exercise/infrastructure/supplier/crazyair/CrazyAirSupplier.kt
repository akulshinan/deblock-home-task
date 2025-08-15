package org.deblock.exercise.infrastructure.supplier.crazyair

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction
import java.math.BigDecimal
import java.time.ZoneOffset
import org.deblock.exercise.application.ports.SupplierPort
import org.deblock.exercise.domain.Airline
import org.deblock.exercise.domain.FlightOffer
import org.deblock.exercise.domain.FlightSearchRequest
import org.deblock.exercise.domain.IataCode
import org.deblock.exercise.domain.Money
import org.deblock.exercise.domain.SupplierName
import org.springframework.stereotype.Component

@Component
class CrazyAirSupplier(
    private val api: CrazyAirApi,
    circuitBreakerRegistry: CircuitBreakerRegistry,
) : SupplierPort {
    private val circuitBreaker = circuitBreakerRegistry.circuitBreaker("crazyAir")

    override val name: String = "CrazyAir"

    override suspend fun getOffers(params: FlightSearchRequest): List<FlightOffer> =
        circuitBreaker.executeSuspendFunction {
            api.search(
                origin = params.origin.value,
                destination = params.destination.value,
                departureDate = params.departureDate.toString(),
                returnDate = params.returnDate.toString(),
                passengerCount = params.passengers.value
            ).map { it.toDomain(params) }
        }

    private fun CrazyAirResponse.toDomain(params: FlightSearchRequest): FlightOffer {
        // didn't find clarification of the price, if it is price for all passengers or for one.
        //  I assume price for all passengers
        //  If price for only one we need to do something like
        //   val unitPrice = Money(BigDecimal.valueOf(price))
        //   val total = (unitPrice * params.passengers.value).rounded2()

        val total = Money(BigDecimal.valueOf(price)).rounded2()
        return FlightOffer(
            airline = Airline(airline),
            supplier = SupplierName(name),
            fare = total,
            departureAirportCode = IataCode(departureAirportCode),
            destinationAirportCode = IataCode(destinationAirportCode),
            departureDate = departureDate.atOffset(ZoneOffset.UTC), // here better to use customer ZoneOffset
            arrivalDate = arrivalDate.atOffset(ZoneOffset.UTC), // here better to use customer ZoneOffset
        )
    }
}
