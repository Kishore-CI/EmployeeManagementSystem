package com.example.EmployeeManagementSystem.Exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;

public class ApiRequestException extends Error{

    private final HttpStatus httpStatus;
    public ApiRequestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
    
    public ApiRequestException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
