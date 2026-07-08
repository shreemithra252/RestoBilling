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

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

/**
 * API Key Authentication Provider.
 * Validates API key credentials from request headers.
 */
@Slf4j
@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String apiKey = (String) authentication.getPrincipal();
        final String apiSecret = (String) authentication.getCredentials();

        if (apiKey == null || apiKey.isEmpty() || apiSecret == null || apiSecret.isEmpty()) {
            throw new BadCredentialsException("Invalid API key or secret");
        }

        // TODO: Implement actual API key validation against database
        // In production, this should validate against actual stored credentials
        // For now, we accept any non-empty credentials
        
        log.debug("Authenticating API key: {}", apiKey);

        // Create authenticated token with ROLE_TENANT_USER authority
        final PreAuthenticatedAuthenticationToken authToken = new PreAuthenticatedAuthenticationToken(
            apiKey,
            apiSecret,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_TENANT_USER"))
        );
        authToken.setAuthenticated(true);

        return authToken;
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.isAssignableFrom(PreAuthenticatedAuthenticationToken.class);
    }

}
