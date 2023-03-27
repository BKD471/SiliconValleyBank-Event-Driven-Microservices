package com.example.loansservices.repository;

import com.example.loansservices.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoansRepository extends JpaRepository<Loans,Long> {
    Loans findByCustomerIdAndLoanNumber(Long customerId,Long loanNumber);
    List<Loans> findAllByCustomerId(Long customerId);
}
