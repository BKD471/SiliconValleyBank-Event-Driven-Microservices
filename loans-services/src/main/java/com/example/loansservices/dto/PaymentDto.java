package com.example.loansservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long loanNumber;
    private Long customerId;
    private String loanType;
    private Double totalLoan;
    private Double paymentAmount;
    private int loanTenureInYears;
    private Double amountPaid;
    private int totalInstallmentsInNumber;
    private int installmentsPaidInNumber;
    private int installmentsRemainingInNumber;
    private Double outstandingAmount;
}
