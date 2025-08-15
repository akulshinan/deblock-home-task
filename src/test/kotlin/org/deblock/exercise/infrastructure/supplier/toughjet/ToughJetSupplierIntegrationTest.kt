package org.deblock.exercise.infrastructure.supplier.toughjet

import java.time.LocalDate
import java.time.OffsetDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.deblock.exercise.WithWireMock
import org.deblock.exercise.application.FlightSearchRequest
import org.deblock.exercise.domain.IataCode
import org.deblock.exercise.domain.PassengerCount
import org.deblock.exercise.initializers.WireMockInstance
import org.deblock.exercise.stub.stubToughJetSuccess
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@WithWireMock
class ToughJetContractTest(
    private val underTest: ToughJetSupplier
) {

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `sends correct query params and maps response with discount and tax`() = runTest {
        stubToughJetSuccess(WireMockInstance.tough)

        val offers = underTest.getOffers(
            FlightSearchRequest(
                IataCode("LHR"),
                IataCode("AMS"),
                LocalDate.parse("2025-08-10"),
                LocalDate.parse("2025-08-12"),
                PassengerCount(2)
            )
        )

        val first = offers.first()
        assertEquals("ToughJet", first.supplier.value)
        assertEquals("Tough", first.airline.value)
        assertEquals("180.00", first.fare.amount.toString())
        assertEquals("LHR", first.departureAirportCode.value)
        assertEquals("AMS", first.destinationAirportCode.value)
        assertEquals(OffsetDateTime.parse("2025-08-10T09:00:00Z"), first.departureDate)
        assertEquals(OffsetDateTime.parse("2025-08-10T11:00:00Z"), first.arrivalDate)
    }
}
