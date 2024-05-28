package com.example.EmployeeManagementSystem.Advice;

import com.example.EmployeeManagementSystem.Exception.ApiException;
import com.example.EmployeeManagementSystem.Exception.ApiRequestException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

//    Handles all the custom exceptions thrown.
    @ExceptionHandler(value = ApiRequestException.class)
    public ResponseEntity<Object> handleApiException(ApiRequestException exception){
        ApiException apiException = new ApiException(
                exception.getMessage(),
                exception.getHttpStatus(),
                ZonedDateTime.now(ZoneId.systemDefault())
        );
        return ResponseEntity.status(exception.getHttpStatus()).body(apiException);
    }

//    Handles all Constraint violation exceptions
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintException(ConstraintViolationException exception){
        Map<Object,Object> exceptionMap = new HashMap<>();
        exception.getConstraintViolations().stream().forEach(e->{
            exceptionMap.put(e.getPropertyPath(),e.getMessage());
        });
        exceptionMap.put("status",HttpStatus.BAD_REQUEST);
        exceptionMap.put("timestamp",ZonedDateTime.now(ZoneId.systemDefault()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMap);
    }

//    Handles all the Method Arguement Type Mismatch exceptions
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatchException(MethodArgumentTypeMismatchException exception){

//        Constructs a custom string that contains the response message
        String message = "Parameter '"+ exception.getPropertyName() +"' requires value of type: "+exception.getRequiredType().getSimpleName()+", but received: "+ exception.getValue().toString();
        ApiException apiException = new ApiException(
                message,
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.systemDefault())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException);
    }

//    Handles all the Illegal Argument exceptions
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArguementException(IllegalArgumentException exception){
        ApiException apiException = new ApiException(
                exception.getLocalizedMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.systemDefault())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException);
    }

//    Handles all the Missing Parameter exceptions
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParameterException(MissingServletRequestParameterException exception){

//        Constructs a custom string that contains the response message
        String message = "Parameter: "+exception.getParameterName()+" is required";
        ApiException apiException = new ApiException(
                message,
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.systemDefault())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException);
    }

//    Handles all the IO exceptions
    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<Object> handleIOException(IOException exception){
        ApiException apiException = new ApiException(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ZonedDateTime.now(ZoneId.systemDefault())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiException);
    }

}
