package org.deblock.exercise.initializers

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration

object WireMockInstance {
    val crazy: WireMockServer = WireMockServer(
        WireMockConfiguration.options().dynamicPort()
    )
        .apply { start() }

    val tough: WireMockServer = WireMockServer(
        WireMockConfiguration.options().dynamicPort()
    ).apply { start() }
}
