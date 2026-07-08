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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for the Tenant Service.
 * Provides centralized error handling and response formatting for all endpoints.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle TenantApiException
     */
    @ExceptionHandler(TenantServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleTenantServiceException(
            final TenantServiceException ex,
            final HttpServletRequest request) {
        
        log.error("Tenant Service exception occurred", ex);
        
        final ErrorResponse error = ErrorResponse.builder()
            .timestamp(java.time.Instant.now())
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .correlationId(MDC.get("correlationId"))
            .build();

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Handle EntityNotFoundException
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            final EntityNotFoundException ex,
            final HttpServletRequest request) {
        
        log.debug("Entity not found: {}", ex.getMessage());
        
        final ErrorResponse error = ErrorResponse.notFound(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setCorrelationId(MDC.get("correlationId"));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handle EntityAlreadyExistsException
     */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExistsException(
            final EntityAlreadyExistsException ex,
            final HttpServletRequest request) {
        
        log.warn("Entity already exists: {}", ex.getMessage());
        
        final ErrorResponse error = ErrorResponse.conflict(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setCorrelationId(MDC.get("correlationId"));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Handle validation exceptions
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(
            final MethodArgumentNotValidException ex,
            final HttpServletRequest request) {
        
        log.warn("Validation failed for request: {}", request.getRequestURI());
        
        final ErrorResponse error = ErrorResponse.badRequest("Validation failed");
        error.setPath(request.getRequestURI());
        error.setCorrelationId(MDC.get("correlationId"));

        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
            error.addFieldError(fieldError.getField(), fieldError.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Handle CircuitBreakerOpenException (service unavailable)
     */
    @ExceptionHandler(CallNotPermittedException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponse> handleCircuitBreakerOpenException(
            final CallNotPermittedException ex,
            final HttpServletRequest request) {
        
        log.error("Circuit breaker is open for: {}", ex.getMessage());
        
        final ErrorResponse error = ErrorResponse.serviceUnavailable(
            "Service temporarily unavailable due to dependency failure. Please retry after some time."
        );
        error.setPath(request.getRequestURI());
        error.setCorrelationId(MDC.get("correlationId"));

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    /**
     * Handle authentication exceptions
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            final AuthenticationException ex,
            final HttpServletRequest request) {
        
        log.warn("Authentication failed: {}", ex.getMessage());
        
        final ErrorResponse error = ErrorResponse.unauthorized("Invalid API credentials");
        error.setPath(request.getRequestURI());
        error.setCorrelationId(MDC.get("correlationId"));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Handle bad credentials
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            final BadCredentialsException ex,
            final HttpServletRequest request) {
        
        log.warn("Bad credentials provided");
        
        final ErrorResponse error = ErrorResponse.unauthorized("Invalid API credentials");
        error.setPath(request.getRequestURI());
        error.setCorrelationId(MDC.get("correlationId"));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Handle IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            final IllegalArgumentException ex,
            final HttpServletRequest request) {
        
        log.warn("Invalid argument: {}", ex.getMessage());
        
        final ErrorResponse error = ErrorResponse.badRequest(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setCorrelationId(MDC.get("correlationId"));

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Handle all other unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            final Exception ex,
            final HttpServletRequest request) {
        
        log.error("Unexpected error occurred", ex);
        
        final ErrorResponse error = ErrorResponse.internalError("Internal server error. Please contact support.");
        error.setPath(request.getRequestURI());
        error.setCorrelationId(MDC.get("correlationId"));
        error.setTraceId(MDC.get("traceId"));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
