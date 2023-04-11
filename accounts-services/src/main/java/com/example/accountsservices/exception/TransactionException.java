package com.example.accountsservices.exception;

public class TransactionException extends Exception{

    private String reason;
    private String methodName;
    public TransactionException(String reason,String methodName){
        super(String.format("TransactionException has occurred in %s for %s",methodName,reason));
        this.reason=reason;
        this.methodName=methodName;
    }
}
