package com.example.accountsservices.repository;

import com.example.accountsservices.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary,Long> {
    void deleteByBeneficiaryId(Long beneficiaryId);
}
