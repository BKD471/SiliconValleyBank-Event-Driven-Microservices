package com.example.accountsservices.dto.token;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtRequest {
    private String email;
    private String password;
}
