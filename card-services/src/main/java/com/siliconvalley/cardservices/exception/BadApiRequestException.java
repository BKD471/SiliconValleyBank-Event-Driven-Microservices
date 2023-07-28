package com.siliconvalley.cardservices.exception;


public class BadApiRequestException extends RuntimeException{

    private final Object className;
    private final String reason;
    private final String methodName;

    public BadApiRequestException(Object className,String reason,String methodName){
        this.className=className;
        this.reason=reason;
        this.methodName=methodName;
    }
}
