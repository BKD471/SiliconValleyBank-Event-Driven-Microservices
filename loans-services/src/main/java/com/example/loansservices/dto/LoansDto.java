package com.example.loansservices.dto;

import com.example.loansservices.model.Loans;
import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoansDto implements   Dto{
    private String loanNumber;
    private String customerId;
    private LocalDate endDt;
    private LocalDate startDt;
    private Loans.LoanType loanType;
    private BigDecimal totalLoan;
    private int loanTenureInYears;
    private BigDecimal amountPaid;
    private BigDecimal emiAmount;
    private Double Rate_of_Interest;
    private int totalInstallmentsInNumber;
    private int installmentsPaidInNumber;
    private int installmentsRemainingInNumber;
    private BigDecimal outstandingAmount;
}
