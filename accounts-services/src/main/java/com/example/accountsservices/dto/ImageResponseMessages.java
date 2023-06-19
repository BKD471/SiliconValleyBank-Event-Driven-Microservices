package com.example.accountsservices.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponseMessages extends  AbstractParentDto{
    private String imageName;
    private String message;
    private boolean success;
    private HttpStatus status;
}
