package com.siliconvalley.accountsservices.dto.responseDtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponseMessages {
    private String imageName;
    private String message;
    private boolean success;
    private HttpStatus status;
}
