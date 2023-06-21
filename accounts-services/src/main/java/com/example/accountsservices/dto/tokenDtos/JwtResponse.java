package com.example.accountsservices.dto.tokenDtos;

import com.example.accountsservices.dto.CustomerDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
  private String jwtToken;
  private CustomerDto customer;
}
