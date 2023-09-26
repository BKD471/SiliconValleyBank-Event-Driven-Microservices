package com.siliconvalley.loansservices.repository;

import com.siliconvalley.loansservices.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.Optional;

@Repository
public interface ILoansRepository extends JpaRepository<Loans,String> {
    Optional<Loans> findByCustomerIdAndLoanNumber(final String customerId, final String loanNumber);
    Optional<Set<Loans>> findAllByCustomerId(final String customerId);
    @Query("SELECT e FROM  Loans e WHERE e.loanNumber=?1 AND e.isLoanActive=?2")
    Optional<Set<Loans>> getAllByCustomerIdForActiveOrInactiveLoans(final String loanNumber,final boolean isLoanActive);
}
