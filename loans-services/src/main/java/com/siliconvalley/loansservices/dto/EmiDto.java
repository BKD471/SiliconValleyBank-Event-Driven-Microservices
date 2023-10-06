package com.siliconvalley.loansservices.dto;

import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmiDto {
    private String loanNumber;
    private String customerId;
    private AllConstantsHelper.LoanType loanType;
    private BigDecimal totalLoan;
    private int loanTenureInYears;
    private BigDecimal paymentAmount;
    private BigDecimal amountPaid;
    private BigDecimal emiAmount;
    private int totalInstallmentsInNumber;
    private int installmentsPaidInNumber;
    private int installmentsRemainingInNumber;
    private BigDecimal outstandingAmount;
    private double rate_of_Interest;
}
