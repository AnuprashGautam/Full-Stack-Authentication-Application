package com.common.auth.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        int statusCode,
        String message,
        HttpStatus status
) {}
