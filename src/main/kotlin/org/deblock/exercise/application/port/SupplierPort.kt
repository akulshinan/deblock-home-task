package org.deblock.exercise.application.port

import org.deblock.exercise.application.FlightSearchRequest
import org.deblock.exercise.domain.FlightOffer

interface SupplierPort {
    val name: String
    suspend fun getOffers(params: FlightSearchRequest): List<FlightOffer>
}
