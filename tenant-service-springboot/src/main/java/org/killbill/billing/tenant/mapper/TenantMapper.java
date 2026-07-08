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

package org.killbill.billing.tenant.mapper;

import org.killbill.billing.tenant.controller.dto.TenantRequest;
import org.killbill.billing.tenant.controller.dto.TenantResponse;

/**
 * Mapper interface for Tenant DTOs.
 * Defines mapping operations between domain models and DTOs.
 */
public interface TenantMapper {

    /**
     * Convert TenantRequest DTO to domain model
     */
    Object requestToDomain(TenantRequest request);

    /**
     * Convert domain model to TenantResponse DTO
     */
    TenantResponse domainToResponse(Object domain);

}
