package com.example.accountsservices.dto.responseDtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class ImageResponseMessages {
    private final String imageName;
    private final String message;
    private final boolean success;
    private final HttpStatus status;
}
