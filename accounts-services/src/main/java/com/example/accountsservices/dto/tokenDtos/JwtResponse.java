package com.example.accountsservices.dto.tokenDtos;

import com.example.accountsservices.dto.baseDtos.CustomerDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class JwtResponse {
  private final String jwtToken;
  private final CustomerDto customer;
}
