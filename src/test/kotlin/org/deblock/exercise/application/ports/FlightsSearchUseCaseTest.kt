package org.deblock.exercise.application.ports

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDate
import java.time.OffsetDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.deblock.exercise.domain.Airline
import org.deblock.exercise.domain.FlightOffer
import org.deblock.exercise.domain.FlightSearchRequest
import org.deblock.exercise.domain.IataCode
import org.deblock.exercise.domain.Money
import org.deblock.exercise.domain.PassengerCount
import org.deblock.exercise.domain.SupplierName
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FlightsSearchUseCaseTest {

    private val crazy: SupplierPort = mockk()
    private val tough: SupplierPort = mockk()

    private val underTest: FlightsSearchUseCase = FlightsSearchUseCase(listOf(crazy, tough))

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `should return Found when suppliers provide flight offers`() = runTest {
        val crazyOffers = listOf(offer(airline = "Crazy", supplier = "CrazyAir", fare = 240.0))
        val toughOffers = listOf(offer(airline = "Tough", supplier = "ToughJet", fare = 180.0))
        coEvery { crazy.getOffers(params) } returns crazyOffers
        coEvery { tough.getOffers(params) } returns toughOffers

        val result = underTest.search(params)

        assertEquals(Found(toughOffers + crazyOffers), result)

        coVerify(exactly = 1) { crazy.getOffers(params) }
        coVerify(exactly = 1) { tough.getOffers(params) }
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `should returns NotFound when no suppliers provide flight offers`() = runTest {
        coEvery { crazy.getOffers(params) } returns emptyList()
        coEvery { tough.getOffers(params) } returns emptyList()

        val result = underTest.search(params)

        assertEquals(NotFound, result)
    }

    @Test
    fun `handles supplier exceptions and returns valid offers from other suppliers`() = runBlocking {
        val toughOffers = listOf(offer(airline = "Tough", supplier = "ToughJet", fare = 180.0))

        coEvery { crazy.getOffers(params) } throws RuntimeException("Error")
        coEvery { tough.getOffers(params) } returns toughOffers

        val result = underTest.search(params)

        assertEquals(Found(toughOffers), result)
    }

    companion object {
        private val params = FlightSearchRequest(
            origin = IataCode("LHR"),
            destination = IataCode("AMS"),
            departureDate = LocalDate.parse("2025-08-10"),
            returnDate = LocalDate.parse("2025-08-12"),
            passengers = PassengerCount(2)
        )

        private fun offer(
            airline: String,
            supplier: String,
            fare: Double,
            dep: String = "2025-08-10T10:00:00Z",
            arr: String = "2025-08-10T12:00:00Z",
            from: String = "LHR",
            to: String = "AMS",
        ) = FlightOffer(
            airline = Airline(airline),
            supplier = SupplierName(supplier),
            fare = Money(fare.toBigDecimal()),
            departureAirportCode = IataCode(from),
            destinationAirportCode = IataCode(to),
            departureDate = OffsetDateTime.parse(dep),
            arrivalDate = OffsetDateTime.parse(arr),
        )
    }
}
