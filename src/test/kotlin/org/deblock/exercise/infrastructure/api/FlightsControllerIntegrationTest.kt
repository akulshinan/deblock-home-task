package org.deblock.exercise.infrastructure.api

import org.deblock.exercise.WithWireMock
import org.deblock.exercise.initializers.WireMockInstance
import org.deblock.exercise.stub.stubCrazyAirSuccess
import org.deblock.exercise.stub.stubToughJetSuccess
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WithWireMock
class FlightsIntegrationTest(private val mockMvc: MockMvc) {

    @Test
    fun `happy path aggregates both suppliers and sorts`() {
        stubCrazyAirSuccess(WireMockInstance.crazy)
        stubToughJetSuccess(WireMockInstance.tough)

        val mvcResult = mockMvc.perform(
            get("/api/v1/flights")
                .param("origin", "LHR")
                .param("destination", "AMS")
                .param("departureDate", "2025-08-10")
                .param("returnDate", "2025-08-12")
                .param("numberOfPassengers", "2")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(request().asyncStarted())   // ← важный шаг
            .andReturn()


        val response = mockMvc.perform(asyncDispatch(mvcResult))  // ← дождаться результата корутины
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith("application/json"))
            .andReturn()
            .response
            .contentAsString

        JSONAssert.assertEquals(
            expectedHappyResponse,
            response,
            JSONCompareMode.STRICT,
        )
    }

    @Test
    fun `should send bad request response parameter is wrong`() {
        stubCrazyAirSuccess(WireMockInstance.crazy)
        stubToughJetSuccess(WireMockInstance.tough)

        val mvcResult = mockMvc.perform(
            get("/api/v1/flights")
                .param("origin", "LHRR")
                .param("destination", "AMS")
                .param("departureDate", "2025-08-10")
                .param("returnDate", "2025-08-12")
                .param("numberOfPassengers", "2")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(request().asyncStarted())   // ← важный шаг
            .andReturn()


        mockMvc.perform(asyncDispatch(mvcResult))  // ← дождаться результата корутины
            .andExpect(status().isBadRequest)

    }


    companion object {
        val expectedHappyResponse = """
            [ 
                {
                    "airline":"Crazy",
                    "supplier":"CrazyAir",
                    "fare":"100.00",
                    "departureAirportCode":"LHR",
                    "destinationAirportCode":"AMS",
                    "departureDate":"2025-08-10T10:00:00.000+0000",
                    "arrivalDate":"2025-08-10T12:00:00.000+0000"
                },
                {   
                    "airline":"Tough",
                    "supplier":"ToughJet",
                    "fare":"180.00",
                    "departureAirportCode":"LHR",
                    "destinationAirportCode":"AMS",
                    "departureDate":"2025-08-10T09:00:00.000+0000",
                    "arrivalDate":"2025-08-10T11:00:00.000+0000"
                }
            ]
        """.trimIndent()
    }
}
