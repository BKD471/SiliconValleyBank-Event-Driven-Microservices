package com.siliconvalley.accountsservices.dto.responseDtos;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseMessages implements Serializable {
    @Serial
    private static final long serialVersionUID=1234567891234567851L;
    private String message;
    private boolean success;
    private HttpStatus status;
}
