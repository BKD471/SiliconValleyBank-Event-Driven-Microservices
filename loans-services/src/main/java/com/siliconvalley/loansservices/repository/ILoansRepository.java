package com.siliconvalley.loansservices.repository;

import com.siliconvalley.loansservices.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ILoansRepository extends JpaRepository<Loans,String> {
    Optional<Loans> findByCustomerIdAndLoanNumber(final String customerId, final String loanNumber);
    Optional<List<Loans>> findAllByCustomerId(final String customerId);
    Optional<List<Loans>> getAllByCustomerIdAndLoanActiveIs(final String loanNumber,final boolean isActive);
}
