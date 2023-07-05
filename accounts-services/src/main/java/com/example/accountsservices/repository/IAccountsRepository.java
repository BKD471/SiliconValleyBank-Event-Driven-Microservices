package com.example.accountsservices.repository;

import com.example.accountsservices.model.Accounts;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface IAccountsRepository extends JpaRepository<Accounts, String> {
    Optional<Accounts> findByAccountNumber(String accountNumber);
    void deleteAllByCustomer_CustomerId(String customerId);
    void deleteByAccountNumber(String accountNumber);
    Optional<Page<Accounts>> findAllByCustomer_CustomerId(String customerId, Pageable pageable);
}
