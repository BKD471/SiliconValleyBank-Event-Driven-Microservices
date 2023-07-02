package com.example.cardservices.exception;

public class CardsException extends RuntimeException{
    private Object className;
    private String reason;
    private String methodName;
    public CardsException(Object className, String rsn, String methodName){
        this.className=className;
        this.reason=rsn;
        this.methodName=methodName;
    }
}
