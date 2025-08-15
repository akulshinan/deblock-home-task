package org.deblock.exercise.initializers

import org.deblock.exercise.initializers.WireMockInstance.crazy
import org.deblock.exercise.initializers.WireMockInstance.tough
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class WireMockInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(context: ConfigurableApplicationContext) {
        val crazyUrl = crazy.baseUrl()
        val toughUrl = tough.baseUrl()
        TestPropertyValues.of(
            "suppliers.crazyAir.baseUrl=$crazyUrl",
            "suppliers.toughJet.baseUrl=$toughUrl",
        ).applyTo(context.environment)
    }
}