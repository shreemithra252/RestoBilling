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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration properties for Tenant Service.
 * Binds to 'killbill.tenant' properties from application configuration files.
 */
@Data
@Component
@ConfigurationProperties(prefix = "killbill.tenant")
public class TenantServiceProperties {

    private Service service = new Service();
    private Cache cache = new Cache();
    private Api api = new Api();
    private Security security = new Security();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Service {
        private String name = "tenant-service";
        private String version = "1.0.0";
        private int requestTimeoutSeconds = 30;
        private boolean enableMetrics = true;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Cache {
        private int ttlMinutes = 60;
        private int maxEntries = 10000;
        private boolean compressionEnabled = false;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Api {
        private String keyHeader = "X-Killbill-ApiKey";
        private String secretHeader = "X-Killbill-ApiSecret";
        private String correlationIdHeader = "X-Correlation-ID";
        private String createdByHeader = "X-Killbill-CreatedBy";
        private String reasonHeader = "X-Killbill-Reason";
        private String commentHeader = "X-Killbill-Comment";
        private int rateLimitPerSecond = 100;
        private int requestSizeLimitMb = 10;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Security {
        private boolean enableApiKeyAuth = true;
        private boolean enableOAuth2 = false;
        private boolean requireHttps = false;
        private boolean apiKeyRotationEnabled = false;
        private int apiKeyExpiryDays = 365;
        private String[] corsAllowedOrigins = {"*"};
        private String[] corsAllowedMethods = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    }

}
