package com.example.accountsservices.repository;

import com.example.accountsservices.model.Beneficiary;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface IBeneficiaryRepository extends JpaRepository<Beneficiary,String> {
    void deleteByBeneficiaryId(String beneficiaryId);
    void deleteAllByAccounts_AccountNumber(String accountNUmber);
    Optional<Page<Beneficiary>> findAllByAccounts_AccountNumber(String AccountNumber, Pageable pageable);
}
