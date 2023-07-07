package com.example.loansservices.dto;

import com.example.loansservices.model.Loans;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto implements Dto {
    private String loanNumber;
    private String customerId;
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
