package com.example.loansservices.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Column(name="end_dt")
    private LocalDateTime endDt;

    @Column(name = "loan_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    public enum LoanType{
        HOUSE_LOAN,CAR_LOAN,EDUCATION_LOAN,BUSINESS_LOAN
    }
    @Column(name = "total_loan")
    private Long totalLoan;

    @Column(name = "loan_tenure")
    private int loanTenureInYears;

    @Column(name = "amount_paid")
    private Long amountPaid;

    @Column(name="rate_of_interest")
    private Double Rate_Of_Interest;

    @Column(name = "emi_amnt")
    private Long emiAmount;

    @Column(name = "tot_inst")
    private int totalInstallmentsInNumber;
    @Column(name = "inst_patd")
    private int installmentsPaidInNumber;

    @Column(name = "inst_rem")
    private int installmentsRemainingInNumber;

    @Column(name = "outstanding_amount")
    private Long outstandingAmount;
}
