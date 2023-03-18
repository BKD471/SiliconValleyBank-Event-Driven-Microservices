package com.example.accountsservices.repository;

import com.example.accountsservices.model.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Accounts,Long> {
    public  Accounts findByCustomerId(Long id);
}
