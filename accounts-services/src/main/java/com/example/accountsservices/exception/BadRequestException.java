package com.example.accountsservices.exception;
public class BadRequestException extends RuntimeException {
    private final String reason;
    private final String methodName;
    private final Object e;

    public BadRequestException(Object e, String reason, String methodName) {
        super(String.format("%s has occurred  for %s in %s",e,reason,methodName));
        this.e = e;
        this.reason = reason;
        this.methodName = methodName;
    }
}
