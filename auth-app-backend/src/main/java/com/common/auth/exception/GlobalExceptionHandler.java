package com.common.auth.exception;

import com.common.auth.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handling ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception)
    {
        ErrorResponse errorResponse = new ErrorResponse(404,exception.getMessage(), HttpStatus.NOT_FOUND);

        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Handle IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception)
    {
        ErrorResponse errorResponse = new ErrorResponse(400,exception.getMessage(), HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
