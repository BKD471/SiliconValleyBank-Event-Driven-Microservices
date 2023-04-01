package com.example.accountsservices.repository;

import com.example.accountsservices.model.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts,Long> {
    Accounts findByCustomerIdAndAccountNumber(Long customerId,Long accountNumber);
    List<Accounts> findAllByCustomerId(Long customerId);
}
