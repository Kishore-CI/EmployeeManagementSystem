package com.example.EmployeeManagementSystem.Advice;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class EmployeeExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String,String> handleInvalidArgumentException(ConstraintViolationException exception){
        Map<String,String> exceptionMap = new HashMap<>();
        exception.getConstraintViolations().forEach(error -> {
            exceptionMap.put(error.getPropertyPath().toString(), error.getMessage());
        });
        return exceptionMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Map<String,String> handleTypeMismatchException(MethodArgumentTypeMismatchException exception){
        Map<String,String> exceptionMap = new HashMap<>();
        exceptionMap.put(exception.getPropertyName(),"Invalid arguement type. Expected type : "+exception.getRequiredType().getSimpleName());
        return exceptionMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ClassCastException.class)
    public Map<String,String> handleClassCastException(ClassCastException exception){
        Map<String,String> exceptionMap = new HashMap<>();
        exceptionMap.put(exception.getMessage(),exception.getLocalizedMessage());
        return exceptionMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullPointerException.class)
    public Map<String,String> handleNullPointerException(NullPointerException exception){
        Map<String,String> exceptionMap = new HashMap<>();
        exceptionMap.put(exception.getMessage(),exception.getMessage());
        return exceptionMap;
    }
}
