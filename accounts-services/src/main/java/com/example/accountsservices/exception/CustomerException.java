package com.example.accountsservices.exception;
public class CustomerException extends Exception {
    private final String reason;
    private final String methodName;
    private final Object e;

    public CustomerException(Object e, String reason, String methodName) {
        super(String.format("%s has occurred  for %s in %s",e,reason,methodName));
        this.e = e;
        this.reason = reason;
        this.methodName = methodName;
    }
}
