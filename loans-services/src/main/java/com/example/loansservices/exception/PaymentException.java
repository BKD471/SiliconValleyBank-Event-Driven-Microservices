package com.example.loansservices.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.time.LocalDateTime;

@NoArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PaymentException extends  Exception{
    private String reason;
    private String methodName;
    private LocalDateTime  t;
    public PaymentException(String reason,String methodName,LocalDateTime t){
        super(String.format("exception has occurred in %s for %s at %s",methodName,reason,t));
        this.reason=reason;
        this.t=t;
        this.methodName=methodName;
    }

}
