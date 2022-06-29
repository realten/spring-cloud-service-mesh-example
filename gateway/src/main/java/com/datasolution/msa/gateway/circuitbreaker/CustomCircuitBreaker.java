package com.datasolution.msa.gateway.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomCircuitBreaker {
    @Bean
    public CircuitBreaker customCircuitBreakerRegistry(CircuitBreakerRegistry circuitBreakerRegistry, CircuitBreakerConfigurationProperties circuitBreakerConfigurationProperties) {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .failureRateThreshold(30)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
        return circuitBreakerRegistry.circuitBreaker("customCircuitBreaker", circuitBreakerConfig);
    }
}
