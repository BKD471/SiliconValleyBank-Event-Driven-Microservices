package com.siliconvalley.accountsservices.dto.baseDtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {
    private String roleId;
    private String roleName;
}
