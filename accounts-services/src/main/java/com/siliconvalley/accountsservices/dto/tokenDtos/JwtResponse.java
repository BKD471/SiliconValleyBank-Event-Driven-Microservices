package com.siliconvalley.accountsservices.dto.tokenDtos;

import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;
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
