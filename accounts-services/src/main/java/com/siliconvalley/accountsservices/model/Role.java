package com.siliconvalley.accountsservices.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="roles")
@Builder
public class Role {
    @Id
    private String roleId;
    private String roleName;
}
