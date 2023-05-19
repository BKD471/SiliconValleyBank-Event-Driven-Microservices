package com.example.accountsservices.repository;

import com.example.accountsservices.model.Beneficiary;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface BeneficiaryRepository extends JpaRepository<Beneficiary,Long> {
    void deleteByBeneficiaryId(Long beneficiaryId);
    void deleteAllByAccounts_AccountNumber(Long accountNUmber);
}
