package org.deblock.exercise.infrastructure.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import java.time.Duration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.deblock.exercise.infrastructure.supplier.crazyair.CrazyAirApi
import org.deblock.exercise.infrastructure.supplier.toughjet.ToughJetApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Configuration
class RetrofitConfig {

    @Bean
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(Duration.ofSeconds(3))
        .readTimeout(Duration.ofSeconds(5))
        .writeTimeout(Duration.ofSeconds(5))
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    @Bean
    fun circuitBreakerRegistry(): CircuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults()

    @Bean
    fun crazyAirApi(
        client: OkHttpClient,
        defaultObjectMapper: ObjectMapper,
        @Value("\${suppliers.crazyAir.baseUrl}") baseUrl: String,
    ): CrazyAirApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(
            JacksonConverterFactory.create(defaultObjectMapper)
        )
        .build()
        .create(CrazyAirApi::class.java)

    @Bean
    fun toughJetApi(
        client: OkHttpClient,
        defaultObjectMapper: ObjectMapper,
        @Value("\${suppliers.toughJet.baseUrl}") baseUrl: String,
    ): ToughJetApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(
            JacksonConverterFactory.create(defaultObjectMapper)
        )
        .build()
        .create(ToughJetApi::class.java)
}