package com.example.loansservices.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LoansException extends  Exception{
    private String reason;
    private String methodName;

    public LoansException(String reason,String methodName){
        super(String.format("exception has occurred in %s for %s",methodName,reason));
        this.reason=reason;
        this.methodName=methodName;
    }

}
