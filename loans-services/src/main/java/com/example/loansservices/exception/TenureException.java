package com.example.loansservices.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@Getter
@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class TenureException extends Exception {
    private String reason;
    private String methodName;


    public TenureException(String reason, String methodName) {
        super(String.format("exception occurred in %s for %s", methodName, reason));
        this.reason = reason;
        this.methodName = methodName;

    }
}
