package com.example.loansservices.mapper;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.dto.PaymentDto;
import com.example.loansservices.model.Loans;

public class LoansMapper {
    public static Loans mapToLoans(final LoansDto loansDto) {
        return Loans.builder()
                .customerId(loansDto.getCustomerId())
                .loanType(loansDto.getLoanType())
                .totalLoan(loansDto.getTotalLoan())
                .loanTenureInYears(loansDto.getLoanTenureInYears())
                .build();
    }

    public static LoansDto mapToLoansDto(final Loans loans) {
        return LoansDto.builder()
                .loanNumber(loans.getLoanNumber())
                .customerId(loans.getCustomerId())
                .loanType(loans.getLoanType())
                .amountPaid(loans.getAmountPaid())
                .outstandingAmount(loans.getOutstandingAmount())
                .loanTenureInYears(loans.getLoanTenureInYears())
                .endDt(loans.getEndDt())
                .startDt(loans.getStartDate())
                .emiAmount(loans.getEmiAmount())
                .Rate_of_Interest(loans.getRate_Of_Interest())
                .totalInstallmentsInNumber(loans.getTotalInstallmentsInNumber())
                .installmentsPaidInNumber(loans.getInstallmentsPaidInNumber())
                .installmentsRemainingInNumber(loans.getInstallmentsRemainingInNumber())
                .build();
    }

    public static PaymentDto mapToPaymentDto(final Loans loans, final Long payment) {
        return PaymentDto.builder()
                .loanNumber(loans.getLoanNumber())
                .customerId(loans.getCustomerId())
                .loanType(loans.getLoanType())
                .totalLoan(loans.getTotalLoan())
                .paymentAmount(payment)
                .loanTenureInYears(loans.getLoanTenureInYears())
                .Rate_Of_Interest(loans.getRate_Of_Interest())
                .amountPaid(loans.getAmountPaid())
                .totalInstallmentsInNumber(loans.getTotalInstallmentsInNumber())
                .installmentsPaidInNumber(loans.getInstallmentsPaidInNumber())
                .installmentsRemainingInNumber(loans.getInstallmentsRemainingInNumber())
                .outstandingAmount(loans.getOutstandingAmount())
                .build();
    }
}
