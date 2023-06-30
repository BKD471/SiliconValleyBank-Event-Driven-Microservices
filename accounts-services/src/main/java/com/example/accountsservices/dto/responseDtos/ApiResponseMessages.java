package com.example.accountsservices.dto.responseDtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseMessages {
    private String message;
    private boolean success;
    private HttpStatus status;
}
