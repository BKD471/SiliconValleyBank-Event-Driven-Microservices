package com.siliconvalley.accountsservices.externalservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
public class ServiceDownException extends RuntimeException{
    private String errorCode;
    private int status;

    public ServiceDownException(String message, String errorCode, int status){
        super(message);
        this.errorCode=errorCode;
        this.status=status;
    }
}
