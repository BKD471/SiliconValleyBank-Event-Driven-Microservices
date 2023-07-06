package com.example.accountsservices.dto.responseDtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class ApiResponseMessages {
    private final String message;
    private final boolean success;
    private final HttpStatus status;
}
