package com.example.accountsservices.dto.tokenDtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class JwtRequest {
    private final String email;
    private final String password;
}
