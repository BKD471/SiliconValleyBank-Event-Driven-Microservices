package com.siliconvalley.accountsservices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class AccountsException extends  RuntimeException {
    public AccountsException(Object className,String reason,String methodName){
        super(String.format("%s has occurred  for %s in %s",className,reason,methodName));
    }
}
