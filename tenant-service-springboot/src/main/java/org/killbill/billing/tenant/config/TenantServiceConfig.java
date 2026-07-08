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

package org.killbill.billing.tenant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.killbill.billing.tenant.service.TenantService;
import org.killbill.billing.tenant.service.TenantServiceImpl;
import org.killbill.billing.tenant.mapper.TenantMapper;
import org.killbill.billing.tenant.mapper.TenantMapperImpl;

import com.redisson.spring.cache.CacheConfig;
import com.redisson.spring.cache.RedissonSpringCacheManager;
import com.redisson.api.RedissonClient;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Configuration for Tenant Service.
 * Defines core beans and service configurations.
 */
@Slf4j
@Configuration
@EnableCaching
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "org.killbill.billing.tenant.repository"
)
@EnableConfigurationProperties(TenantServiceProperties.class)
public class TenantServiceConfig {

    /**
     * Configure the primary cache manager using Redisson
     */
    @Bean
    @Primary
    public CacheManager cacheManager(final RedissonClient redissonClient,
                                    final TenantServiceProperties tenantProperties) {
        log.info("Configuring Redisson cache manager");
        
        final java.util.Map<String, CacheConfig> config = new java.util.HashMap<>();
        
        // Cache configuration for tenant KV storage
        config.put("tenant_kv_cache", new CacheConfig()
            .setTtl(tenantProperties.getCache().getTtlMinutes() * 60 * 1000)
            .setMaxIdleTime(tenantProperties.getCache().getTtlMinutes() * 60 * 1000)
            .setMaxSize(tenantProperties.getCache().getMaxEntries())
        );
        
        // Cache for tenant entities
        config.put("tenant_cache", new CacheConfig()
            .setTtl(tenantProperties.getCache().getTtlMinutes() * 60 * 1000)
            .setMaxSize(tenantProperties.getCache().getMaxEntries())
        );
        
        // Cache for plugin configurations
        config.put("plugin_config_cache", new CacheConfig()
            .setTtl((tenantProperties.getCache().getTtlMinutes() * 60 * 1000) / 2)
            .setMaxSize(5000)
        );

        return new RedissonSpringCacheManager(redissonClient, config);
    }

    /**
     * Tenant service bean - provides business logic
     */
    @Bean
    public TenantService tenantService() {
        log.info("Initializing TenantService bean");
        return new TenantServiceImpl();
    }

    /**
     * Tenant mapper bean for DTO conversions
     */
    @Bean
    public TenantMapper tenantMapper() {
        log.info("Initializing TenantMapper bean");
        return new TenantMapperImpl();
    }

    /**
     * Register shutdown hook for graceful service termination
     */
    @Bean
    public TenantServiceShutdownHook shutdownHook(final TenantService tenantService) {
        log.info("Registering graceful shutdown hook");
        return new TenantServiceShutdownHook(tenantService);
    }

}
