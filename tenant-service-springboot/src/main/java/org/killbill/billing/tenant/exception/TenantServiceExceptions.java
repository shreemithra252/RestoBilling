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

package org.killbill.billing.tenant.exception;

/**
 * Base exception for Tenant Service
 */
public class TenantServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TenantServiceException(final String message) {
        super(message);
    }

    public TenantServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TenantServiceException(final Throwable cause) {
        super(cause);
    }
}

/**
 * Exception thrown when an entity is not found
 */
class EntityNotFoundException extends TenantServiceException {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(final String message) {
        super(message);
    }

    public EntityNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

/**
 * Exception thrown when an entity already exists
 */
class EntityAlreadyExistsException extends TenantServiceException {

    private static final long serialVersionUID = 1L;

    public EntityAlreadyExistsException(final String message) {
        super(message);
    }

    public EntityAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
