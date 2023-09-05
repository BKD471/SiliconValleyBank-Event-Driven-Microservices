package com.siliconvalley.accountsservices.repository;

import com.siliconvalley.accountsservices.model.Accounts;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface IAccountsRepository extends JpaRepository<Accounts, String> {
    Optional<Accounts> findByAccountNumber(final String accountNumber);
    void deleteAllByCustomer_CustomerId(final String customerId);
    void deleteByAccountNumber(final String accountNumber);
   Optional<Page<Accounts>> findAllByCustomer_CustomerId(final String customerId,final Pageable pageable);
}
