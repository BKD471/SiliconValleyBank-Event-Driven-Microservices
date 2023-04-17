package com.example.accountsservices.repository;

import com.example.accountsservices.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary,Long> {
    void deleteByBeneficiaryId(Long beneficiaryId);
}
