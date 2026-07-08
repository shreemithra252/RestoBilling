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

import org.springframework.stereotype.Component;

import org.killbill.billing.tenant.service.TenantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;

/**
 * Graceful shutdown hook for Tenant Service.
 * Ensures proper cleanup of resources on application termination.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TenantServiceShutdownHook {

    private final TenantService tenantService;

    @PreDestroy
    public void onApplicationShutdown() {
        log.info("Tenant Service shutting down gracefully");
        
        try {
            // Perform cleanup operations
            tenantService.shutdown();
            log.info("Tenant Service shutdown completed successfully");
        } catch (final Exception e) {
            log.error("Error during Tenant Service shutdown", e);
        }
    }

}
