package com.siliconvalley.accountsservices.dto.tokenDtos;

import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse implements Serializable {
  @Serial
  private static final long serialVersionUID=1234567891234567112L;
  private String jwtToken;
  private CustomerDto customer;
}
