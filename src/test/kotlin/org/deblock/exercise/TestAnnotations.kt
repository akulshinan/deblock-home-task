package org.deblock.exercise

import org.deblock.exercise.infrastructure.config.JacksonConfig
import org.deblock.exercise.infrastructure.config.RetrofitConfig
import org.deblock.exercise.initializers.WireMockInitializer
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor

@SpringBootTest(classes = [ExerciseApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
annotation class IntegrationTest

@IntegrationTest
@ContextConfiguration(initializers = [WireMockInitializer::class])
@Import(value = [JacksonConfig::class, RetrofitConfig::class])
annotation class WithWireMock


