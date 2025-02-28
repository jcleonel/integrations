package com.sensedia.desafio.integrations.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(),"NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RequiredObjectIsNullException.class)
    public final ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception ex, WebRequest request) {
        ErrorResponse exceptionResponse = new ErrorResponse(new Date(), "BAD_REQUEST", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public final ResponseEntity<ErrorResponse> handleInsufficientStockException(Exception ex, WebRequest request) {
        ErrorResponse exceptionResponse = new ErrorResponse(new Date(), "BAD_REQUEST", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public final ResponseEntity<ErrorResponse> handleUnsupportedOperationException(Exception ex, WebRequest request) {
        ErrorResponse exceptionResponse = new ErrorResponse(new Date(), "BAD_REQUEST", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public final ResponseEntity<ErrorResponse> handleInvalidJwtAuthenticationException(Exception ex, WebRequest request) {
        ErrorResponse exceptionResponse = new ErrorResponse(new Date(), "FORBIDDEN", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidClientException.class)
    public final ResponseEntity<ErrorResponse> handleCredentialsInvalidException(Exception ex, WebRequest request) {
        ErrorResponse exceptionResponse = new ErrorResponse(new Date(), "FORBIDDEN", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(),"INTERNAL_SERVER_ERROR", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
