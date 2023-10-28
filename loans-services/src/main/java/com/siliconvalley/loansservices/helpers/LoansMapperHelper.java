package com.siliconvalley.loansservices.helpers;

import com.siliconvalley.loansservices.dto.EmiDto;
import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.model.Loans;

import java.math.BigDecimal;

public class LoansMapperHelper {
    public static Loans mapToLoans(final LoansDto loansDto) {
        return Loans.builder()
                .customerId(loansDto.customerId())
                .loanType(loansDto.loanType())
                .totalLoan(loansDto.totalLoan())
                .loanTenureInYears(loansDto.loanTenureInYears())
                .Rate_Of_Interest(null)
                .totalInstallmentsInNumber(null)
                .installmentsPaidInNumber(null)
                .installmentsRemainingInNumber(null)
                .isLoanActive(null).build();
    }

    public static LoansDto mapToLoansDto(final Loans loans) {
        return new LoansDto.Builder()
                .loanNumber(loans.getLoanNumber())
                .customerId(loans.getCustomerId())
                .loanType(loans.getLoanType())
                .totalLoan(loans.getTotalLoan())
                .amountPaid(loans.getAmountPaid())
                .outstandingAmount(loans.getOutstandingAmount())
                .loanTenureInYears(loans.getLoanTenureInYears())
                .endDate(loans.getEndDt())
                .startDate(loans.getStartDate())
                .emiAmount(loans.getEmiAmount())
                .Rate_of_Interest(loans.getRate_Of_Interest())
                .totalInstallmentsInNumber(loans.getTotalInstallmentsInNumber())
                .installmentsPaidInNumber(loans.getInstallmentsPaidInNumber())
                .installmentsRemainingInNumber(loans.getInstallmentsRemainingInNumber())
                .build();
    }

    public static EmiDto mapToEmiDto(final Loans loans,final BigDecimal paymentAmount ){
        return EmiDto.builder()
                .loanNumber(loans.getLoanNumber())
                .customerId(loans.getCustomerId())
                .loanType(loans.getLoanType())
                .totalLoan(loans.getTotalLoan())
                .loanTenureInYears(loans.getLoanTenureInYears())
                .paymentAmount(paymentAmount)
                .amountPaid(loans.getAmountPaid())
                .emiAmount(loans.getEmiAmount())
                .totalInstallmentsInNumber(loans.getTotalInstallmentsInNumber())
                .installmentsPaidInNumber(loans.getInstallmentsPaidInNumber())
                .installmentsRemainingInNumber(loans.getInstallmentsRemainingInNumber())
                .outstandingAmount(loans.getOutstandingAmount())
                .rate_of_Interest(loans.getRate_Of_Interest())
                .build();
    }
}
