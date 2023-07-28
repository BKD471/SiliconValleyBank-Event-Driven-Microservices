package com.siliconvalley.cardservices.exception;

public class CardsException extends RuntimeException{
    private final Object className;
    private final String reason;
    private final String methodName;
    public CardsException(Object className, String rsn, String methodName){
        this.className=className;
        this.reason=rsn;
        this.methodName=methodName;
    }
}
