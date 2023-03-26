package com.example.loansservices.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;


@ResponseStatus(value= HttpStatus.BAD_REQUEST)
@NoArgsConstructor
public class TenureException extends Exception {
    private String reason;
    private String methodName;
    private LocalDateTime timeStamp;

    public TenureException(String reason, String methodName, LocalDateTime timeStamp) {
        super(String.format("exception occurred in %s for %s at %s", methodName, reason, timeStamp));
        this.reason = reason;
        this.methodName = methodName;
        this.timeStamp = timeStamp;
    }
}
