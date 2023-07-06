package com.example.accountsservices.dto.baseDtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class RoleDto {
    private final String roleId;
    private final String roleName;
}
