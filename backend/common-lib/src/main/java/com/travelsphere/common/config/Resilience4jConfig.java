package com.travelsphere.common.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Programmatic circuit breaker customizations that complement the YAML-based
 * defaults in config-repo/application.yml.
 *
 * Circuit breaker naming convention used:
 * - Feign clients: auto-generated from @FeignClient name via alphanumeric-ids
 * - @CircuitBreaker annotations: "booking-verification", "ollama-api"
 */
@Configuration
public class Resilience4jConfig {

    /**
     * Circuit breaker for Ollama AI external API calls.
     * Relaxed timeouts since LLM inference can be slow.
     */
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> ollamaApiCircuitBreakerCustomizer() {
        return factory -> factory.configure(builder -> builder
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .slidingWindowSize(5)
                        .minimumNumberOfCalls(3)
                        .failureRateThreshold(60)
                        .waitDurationInOpenState(Duration.ofSeconds(60))
                        .permittedNumberOfCallsInHalfOpenState(2)
                        .slowCallRateThreshold(70)
                        .slowCallDurationThreshold(Duration.ofSeconds(15))
                        .build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(30))
                        .cancelRunningFuture(true)
                        .build())
                .build(), "ollama-api");
    }

    /**
     * Circuit breaker for booking verification calls across services.
     * These are payment critical-path calls that should fail fast.
     */
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> bookingVerificationCircuitBreakerCustomizer() {
        return factory -> factory.configure(builder -> builder
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .slidingWindowSize(10)
                        .minimumNumberOfCalls(5)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofSeconds(15))
                        .permittedNumberOfCallsInHalfOpenState(3)
                        .slowCallRateThreshold(60)
                        .slowCallDurationThreshold(Duration.ofSeconds(3))
                        .build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(3))
                        .cancelRunningFuture(true)
                        .build())
                .build(), "booking-verification");
    }
}
