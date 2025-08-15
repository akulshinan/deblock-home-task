package org.deblock.exercise.domain

import java.math.BigDecimal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ValueTypesTest {
    @Test
    fun `IataCode validates`() {
        assertThrows<IllegalArgumentException> { IataCode("LhR") }
    }

    @Test
    fun `passengerCount range`() {
        assertThrows<IllegalArgumentException> { PassengerCount(0) }
    }

    @Test
    fun `money arithmetic`() {
        val money = Money(BigDecimal("10.123")).rounded2()
        assertEquals("10.12", money.amount.toString())
    }
}
