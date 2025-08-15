package org.deblock.exercise.application

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import org.deblock.exercise.application.port.SupplierPort
import org.springframework.stereotype.Service

@Service
class FlightsSearchUseCase(
    private val suppliers: List<SupplierPort>,
) {
    suspend fun search(params: FlightSearchRequest): SearchResult = supervisorScope {
        val result = suppliers.map { supplier ->
            async {
                runCatching { supplier.getOffers(params) }
                    .getOrElse { emptyList() }
            }
        }.awaitAll().asSequence()
            .flatten()
            .sortedBy { it.fare.amount }.toList()

        if (result.isEmpty()) {
            NotFound
        } else {
            Found(result)
        }
    }
}