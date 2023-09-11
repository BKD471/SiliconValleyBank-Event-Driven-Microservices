package com.siliconvalley.loansservices.dto;

import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import com.siliconvalley.loansservices.model.Loans;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;


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
    private AllConstantsHelper.LoanType loanType;
    private BigDecimal totalLoan;
    private int loanTenureInYears;
    private BigDecimal paymentAmount;
    private BigDecimal amountPaid;
    private BigDecimal emiAmount;
    private Double Rate_of_Interest;
    private int totalInstallmentsInNumber;
    private int installmentsPaidInNumber;
    private int installmentsRemainingInNumber;
    private BigDecimal outstandingAmount;
    private AllConstantsHelper.RequestType requestType;
    private Set<Loans> loansSet;
}
