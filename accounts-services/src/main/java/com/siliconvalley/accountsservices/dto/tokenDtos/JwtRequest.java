package com.siliconvalley.accountsservices.dto.tokenDtos;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtRequest implements Serializable {
    @Serial
    private static final long serialVersionUID=1234567891234567111L;
    private String email;
    private String password;
}
