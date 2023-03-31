package com.example.loansservices.dto;

import com.example.loansservices.model.Loans;
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
public class PaymentDto extends Dto {
    private Long loanNumber;
    private Long customerId;
    private Loans.LoanType loanType;
    private Long totalLoan;
    private Long paymentAmount;
    private int loanTenureInYears;
    private Long amountPaid;
    private Double Rate_Of_Interest;
    private int totalInstallmentsInNumber;
    private int installmentsPaidInNumber;
    private int installmentsRemainingInNumber;
    private Long outstandingAmount;
}
