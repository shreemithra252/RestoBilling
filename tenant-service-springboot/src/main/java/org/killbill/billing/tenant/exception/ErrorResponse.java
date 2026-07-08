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

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Standard error response structure for API errors.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private Instant timestamp;
    private int statusCode;
    private String message;
    private String description;
    private String path;
    private String correlationId;
    private List<FieldError> fieldErrors;
    private String traceId;

    /**
     * Create a bad request error response
     */
    public static ErrorResponse badRequest(final String message) {
        return ErrorResponse.builder()
            .timestamp(Instant.now())
            .statusCode(400)
            .message(message)
            .build();
    }

    /**
     * Create a not found error response
     */
    public static ErrorResponse notFound(final String message) {
        return ErrorResponse.builder()
            .timestamp(Instant.now())
            .statusCode(404)
            .message(message)
            .build();
    }

    /**
     * Create a conflict error response (e.g., duplicate resource)
     */
    public static ErrorResponse conflict(final String message) {
        return ErrorResponse.builder()
            .timestamp(Instant.now())
            .statusCode(409)
            .message(message)
            .build();
    }

    /**
     * Create an internal server error response
     */
    public static ErrorResponse internalError(final String message) {
        return ErrorResponse.builder()
            .timestamp(Instant.now())
            .statusCode(500)
            .message(message)
            .build();
    }

    /**
     * Create a service unavailable error response (e.g., circuit breaker open)
     */
    public static ErrorResponse serviceUnavailable(final String message) {
        return ErrorResponse.builder()
            .timestamp(Instant.now())
            .statusCode(503)
            .message(message)
            .build();
    }

    /**
     * Create an unauthorized error response
     */
    public static ErrorResponse unauthorized(final String message) {
        return ErrorResponse.builder()
            .timestamp(Instant.now())
            .statusCode(401)
            .message(message)
            .build();
    }

    /**
     * Create a forbidden error response
     */
    public static ErrorResponse forbidden(final String message) {
        return ErrorResponse.builder()
            .timestamp(Instant.now())
            .statusCode(403)
            .message(message)
            .build();
    }

    /**
     * Add a field validation error
     */
    public void addFieldError(final String field, final String message) {
        if (this.fieldErrors == null) {
            this.fieldErrors = new ArrayList<>();
        }
        this.fieldErrors.add(new FieldError(field, message));
    }

    /**
     * Nested class for field-level validation errors
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FieldError {
        private String field;
        private String message;
    }

}
