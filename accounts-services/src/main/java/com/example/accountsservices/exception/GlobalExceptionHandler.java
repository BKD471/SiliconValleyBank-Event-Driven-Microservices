package com.example.accountsservices.exception;

import com.example.accountsservices.dto.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AccountsException.class,BeneficiaryException.class, TransactionException.class, ResponseException.class, CustomerException.class})
    public ResponseEntity<ErrorDetails> handleAllCustomException(Exception e, WebRequest web){
        ErrorDetails error=new ErrorDetails(LocalTime.now(),e.getMessage(), web.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGenericException(Exception e, WebRequest web){
        ErrorDetails error=new ErrorDetails(LocalTime.now(),e.getMessage(), web.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
