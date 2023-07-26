package com.siliconvalley.accountsservices.repository;

import com.siliconvalley.accountsservices.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role,String> {
}
