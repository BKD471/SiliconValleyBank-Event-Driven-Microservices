package com.siliconvalley.accountsservices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BeneficiaryException extends RuntimeException {
    public BeneficiaryException(Object className,String reason,String methodName){
       super(String.format("%s has occurred  for %s in %s",className,reason,methodName));
    }
}
