package com.example.accountsservices.exception;
public class CustomerException extends RuntimeException {
    private final String reason;
    private final String methodName;
    private final Object className;

    public CustomerException(Object className, String reason, String methodName) {
        super(String.format("%s has occurred  for %s in %s",className,reason,methodName));
        this.className = className;
        this.reason = reason;
        this.methodName = methodName;
    }
}
