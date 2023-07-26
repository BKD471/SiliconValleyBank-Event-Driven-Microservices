package com.siliconvalley.loansservices.dto;

import com.siliconvalley.loansservices.model.Loans;
import lombok.*;

import java.math.BigDecimal;

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
