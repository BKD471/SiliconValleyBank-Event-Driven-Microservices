package com.example.accountsservices.exception;

public class ResponseException extends Exception{
    private final String reason;
    private final String methodName;
    private final Object obj;
    public ResponseException(Object obj,String reason, String methodName){
        super(String.format("%s has occurred  for %s in %s",obj,reason,methodName));
        this.reason=reason;
        this.methodName=methodName;
        this.obj=obj;
    }
}
