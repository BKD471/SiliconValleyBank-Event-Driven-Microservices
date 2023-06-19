package com.example.accountsservices.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseMessages extends AbstractParentDto{
    private String message;
    private boolean success;
    private HttpStatus status;
}
