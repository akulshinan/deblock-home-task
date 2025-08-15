package org.deblock.exercise.application.ports

import org.deblock.exercise.domain.FlightOffer

sealed class SearchResult

data class Found(val offers: List<FlightOffer>) : SearchResult()
object NotFound : SearchResult()
