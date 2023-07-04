package com.example.cardservices.exception;

public class TenureException extends RuntimeException{
    private final Object className;

    private final String method;
    private final String reason;
    public TenureException(Object className,String reason,String methodName){
        this.className=className;
        this.reason=reason;
        this.method=methodName;
    }
}
