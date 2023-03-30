package com.example.loansservices.dto;

import com.example.loansservices.model.Loans;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoansDto extends  Dto{
    private Long loanNumber;
    private Long customerId;
    private LocalDateTime endDt;
    private LocalDateTime startDt;
    private Loans.LoanType loanType;
    private Long totalLoan;
    private int loanTenureInYears;
    private Long amountPaid;
    private Long emiAmount;
    private Double Rate_of_Interest;
    private int totalInstallmentsInNumber;
    private int installmentsPaidInNumber;
    private int installmentsRemainingInNumber;
    private Long outstandingAmount;
}
