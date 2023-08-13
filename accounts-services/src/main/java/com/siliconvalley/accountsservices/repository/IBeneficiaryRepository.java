package com.siliconvalley.accountsservices.repository;

import com.siliconvalley.accountsservices.model.Beneficiary;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface IBeneficiaryRepository extends JpaRepository<Beneficiary,String> {
    @Modifying
    void deleteAllByAccounts_AccountNumber(final String accountNUmber);

    @Transactional
    void deleteAllByIdInBatch(Iterable<String> beneficiaryId);

    Optional<Page<Beneficiary>> findAllByAccounts_AccountNumber(final String AccountNumber, final Pageable pageable);
}
