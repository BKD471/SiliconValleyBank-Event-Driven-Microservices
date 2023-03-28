package com.example.loansservices.dto;

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
    private String loanType;
    private Double totalLoan;
    private int loanTenureInYears;
    private Double amountPaid;
    private Double emiAmount;
    private Double Rate_of_Interest;
    private int totalInstallmentsInNumber;
    private int installmentsPaidInNumber;
    private int installmentsRemainingInNumber;
    private Double outstandingAmount;
}
