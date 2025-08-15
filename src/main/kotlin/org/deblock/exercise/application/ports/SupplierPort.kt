package org.deblock.exercise.application.ports

import org.deblock.exercise.domain.FlightOffer
import org.deblock.exercise.domain.FlightSearchRequest

interface SupplierPort {
    val name: String
    suspend fun getOffers(params: FlightSearchRequest): List<FlightOffer>
}
