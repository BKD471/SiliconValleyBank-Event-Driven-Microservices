package com.example.loansservices.repository;

import com.example.loansservices.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoansRepository extends JpaRepository<Loans,Long> {
}
