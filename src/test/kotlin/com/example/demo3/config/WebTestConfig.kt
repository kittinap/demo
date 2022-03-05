package com.example.demo3.config

import io.micrometer.core.instrument.MeterRegistry
import org.mockito.Mockito
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(JacksonConfiguration::class)
class WebTestConfig{

    @Bean
    @ConditionalOnMissingBean
    fun meterRegistryMock(): MeterRegistry {
        return Mockito.mock(MeterRegistry::class.java)
    }
}
