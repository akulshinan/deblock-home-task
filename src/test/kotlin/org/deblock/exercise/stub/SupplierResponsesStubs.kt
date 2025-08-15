package org.deblock.exercise.stub

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo

fun stubCrazyAirSuccess(server: WireMockServer) {
    server.stubFor(
        get(urlPathEqualTo("/crazyair/flights"))
            .withQueryParam("origin", equalTo("LHR"))
            .withQueryParam("destination", equalTo("AMS"))
            .withQueryParam("departureDate", equalTo("2025-08-10"))
            .withQueryParam("returnDate", equalTo("2025-08-12"))
            .withQueryParam("passengerCount", equalTo("2"))
            .willReturn(
                okJson(
                    """
                [
                  {
                    "airline":"Crazy",
                     "price":100.0,
                     "cabinclass":"E",
                     "departureAirportCode":"LHR",
                     "destinationAirportCode":"AMS",
                     "departureDate":"2025-08-10T10:00:00",
                     "arrivalDate":"2025-08-10T12:00:00"
                  }
                ]
                """.trimIndent()
                )
            )
    )
}

fun stubToughJetSuccess(server: WireMockServer) {
    server.stubFor(
        get(urlPathEqualTo("/toughjet/flights"))
            .withQueryParam("from", equalTo("LHR"))
            .withQueryParam("to", equalTo("AMS"))
            .withQueryParam("outboundDate", equalTo("2025-08-10"))
            .withQueryParam("inboundDate", equalTo("2025-08-12"))
            .withQueryParam("numberOfAdults", equalTo("2"))
            .willReturn(
                okJson(
                    """
                [
                  {
                    "carrier":"Tough",
                     "basePrice": 80.0, 
                     "tax": 10.0, 
                     "discount": 0.0, 
                     "departureAirportName":"LHR",
                     "arrivalAirportName":"AMS",
                     "outboundDateTime":"2025-08-10T09:00:00Z",
                     "inboundDateTime":"2025-08-10T11:00:00Z"
                  }
                ]
                """.trimIndent()
                )
            )
    )
}
