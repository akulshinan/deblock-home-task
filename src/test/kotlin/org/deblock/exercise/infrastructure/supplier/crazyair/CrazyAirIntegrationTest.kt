package org.deblock.exercise.infrastructure.supplier.crazyair

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.deblock.exercise.WithWireMock
import org.deblock.exercise.domain.FlightSearchRequest
import org.deblock.exercise.domain.IataCode
import org.deblock.exercise.domain.PassengerCount
import org.deblock.exercise.initializers.WireMockInstance
import org.deblock.exercise.stub.stubCrazyAirSuccess
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@WithWireMock
class CrazyAirIntegrationTest(
    private val underTest: CrazyAirSupplier
) {
    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `sends correct query params and maps response`() = runTest {
        stubCrazyAirSuccess(WireMockInstance.crazy)

        val offers = underTest.getOffers(
            FlightSearchRequest(
                IataCode("LHR"), IataCode("AMS"),
                LocalDate.parse("2025-08-10"), LocalDate.parse("2025-08-12"),
                PassengerCount(2)
            )
        )

        val first = offers.first()
        // size
        assertEquals(1, offers.size)
        assertEquals("CrazyAir", first.supplier.value)
        assertEquals("Crazy", first.airline.value)
        assertEquals("100.00", first.fare.amount.toString())
        assertEquals("LHR", first.departureAirportCode.value)
        assertEquals("AMS", first.destinationAirportCode.value)
        val expectedDep = OffsetDateTime.of(2025, 8, 10, 10, 0, 0, 0, ZoneOffset.UTC)
        val expectedArr = OffsetDateTime.of(2025, 8, 10, 12, 0, 0, 0, ZoneOffset.UTC)
        assertEquals(expectedDep, first.departureDate)
        assertEquals(expectedArr, first.arrivalDate)
    }
}