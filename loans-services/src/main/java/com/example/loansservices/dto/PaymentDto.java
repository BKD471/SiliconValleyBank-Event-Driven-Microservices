package com.example.loansservices.dto;

import com.example.loansservices.model.Loans;
import lombok.*;

import java.math.BigDecimal;
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
    private BigDecimal totalLoan;
    private BigDecimal paymentAmount;
    private int loanTenureInYears;
    private BigDecimal amountPaid;
    private Double Rate_Of_Interest;
    private int totalInstallmentsInNumber;
    private int installmentsPaidInNumber;
    private int installmentsRemainingInNumber;
    private BigDecimal outstandingAmount;
}
