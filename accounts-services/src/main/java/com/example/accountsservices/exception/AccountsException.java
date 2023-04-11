package com.example.accountsservices.exception;

public class AccountsException extends  Exception{
    private String reason;
    private String methodName;
    public AccountsException(String reason,String methodName){
        super(String.format("AccountsException has occurred in %s for %s",methodName,reason));
        this.reason=reason;
        this.methodName=methodName;
    }
}
