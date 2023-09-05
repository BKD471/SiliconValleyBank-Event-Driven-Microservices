package com.siliconvalley.accountsservices.dto.baseDtos;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto implements Serializable {
    @Serial
    private static final long serialVersionUID=1234567891234567895L;
    private String roleId;
    private String roleName;
}
