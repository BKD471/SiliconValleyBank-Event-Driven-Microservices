package com.example.loansservices.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.time.LocalDateTime;

@NoArgsConstructor
@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class InstallmentsException extends  Exception{
    private String reason;
    private String methodName;
    private LocalDateTime timeStamp;
    public InstallmentsException(String reason, String methodName, LocalDateTime timeStamp)
    {
        super(String.format("exception has occurred in %s for %s at %s",methodName,reason,timeStamp));
        this.reason=reason;
        this.methodName=methodName;
        this.timeStamp=timeStamp;
    }
}
