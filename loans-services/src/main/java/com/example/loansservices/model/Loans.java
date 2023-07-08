package com.example.loansservices.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Loans extends Audit{
    @Id
    private String loanNumber;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name="end_dt")
    private LocalDate endDt;

    @Column(name = "loan_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    public enum LoanType{
        HOUSE_LOAN,CAR_LOAN,EDUCATION_LOAN,BUSINESS_LOAN
    }
    @Column(name = "total_loan")
    private long totalLoan;

    @Column(name = "loan_tenure")
    private int loanTenureInYears;

    @Column(name = "amount_paid")
    private long amountPaid;

    @Column(name="rate_of_interest")
    private double Rate_Of_Interest;

    @Column(name = "emi_amnt")
    private long emiAmount;

    @Column(name = "tot_inst")
    private int totalInstallmentsInNumber;
    @Column(name = "inst_patd")
    private int installmentsPaidInNumber;

    @Column(name = "inst_rem")
    private int installmentsRemainingInNumber;

    @Column(name = "outstanding_amount")
    private long outstandingAmount;
}
