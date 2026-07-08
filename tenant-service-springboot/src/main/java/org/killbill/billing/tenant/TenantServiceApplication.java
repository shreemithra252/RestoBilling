/*
 * Copyright 2014-2026 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.tenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Boot application entry point for Killbill Tenant Service.
 * 
 * This microservice provides standalone tenant management capabilities including:
 * - Tenant creation and retrieval
 * - Per-tenant configuration management
 * - Plugin configuration per tenant
 * - Key-value storage for tenant-specific data
 * - Cache invalidation and management
 * 
 * @author Killbill Contributors
 * @version 1.0.0
 */
@Slf4j
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class TenantServiceApplication {

    public static void main(final String[] args) {
        log.info("Starting Killbill Tenant Service...");
        SpringApplication.run(TenantServiceApplication.class, args);
        log.info("Killbill Tenant Service started successfully");
    }

    /**
     * Registry event consumer for Resilience4J circuit breakers.
     * Logs circuit breaker state changes for monitoring and debugging.
     *
     * @return RegistryEventConsumer for circuit breaker events
     */
    @Bean
    public RegistryEventConsumer<CircuitBreakerConfig> circuitBreakerEventConsumer() {
        return new RegistryEventConsumer<CircuitBreakerConfig>() {
            @Override
            public void onEntryAddedEvent(final EntryAddedEvent<CircuitBreakerConfig> entryAddedEvent) {
                log.debug("Circuit breaker added: {}", entryAddedEvent.getAddedEntry().getName());
            }

            @Override
            public void onEntryRemovedEvent(final EntryRemovedEvent<CircuitBreakerConfig> entryRemoved) {
                log.debug("Circuit breaker removed: {}", entryRemoved.getRemovedEntry().getName());
            }

            @Override
            public void onEntryReplacedEvent(final EntryReplacedEvent<CircuitBreakerConfig> entryReplacedEvent) {
                log.debug("Circuit breaker replaced: {}", entryReplacedEvent.getAddedEntry().getName());
            }
        };
    }

}
