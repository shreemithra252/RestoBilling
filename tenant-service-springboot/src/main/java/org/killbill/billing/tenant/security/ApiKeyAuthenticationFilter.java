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

package org.killbill.billing.tenant.security;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import org.killbill.billing.tenant.config.TenantServiceProperties;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * API Key Authentication Filter.
 * Extracts API key and secret from request headers for authentication.
 */
@Slf4j
public class ApiKeyAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private final TenantServiceProperties tenantServiceProperties;

    public ApiKeyAuthenticationFilter(final TenantServiceProperties tenantServiceProperties) {
        this.tenantServiceProperties = tenantServiceProperties;
        setCheckForPrincipalChanges(true);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(final HttpServletRequest request) {
        final String apiKey = request.getHeader(tenantServiceProperties.getApi().getKeyHeader());
        if (apiKey != null && !apiKey.isEmpty()) {
            log.debug("API Key found in request header");
            return apiKey;
        }
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(final HttpServletRequest request) {
        final String apiSecret = request.getHeader(tenantServiceProperties.getApi().getSecretHeader());
        if (apiSecret != null && !apiSecret.isEmpty()) {
            return apiSecret;
        }
        return null;
    }

}
