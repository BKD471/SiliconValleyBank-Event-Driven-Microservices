package com.example.loansservices.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Loans extends Audit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanNumber;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name="start_dt")
    private LocalDateTime startDt;

    @Column(name = "loan_type")
    private String loanType;

    @Column(name = "total_loan")
    private Long totalLoan;

    @Column(name = "amount_paid")
    private Long amountPaid;

    @Column(name = "outstanding_amount")
    private Long outstandingAmount;
}
