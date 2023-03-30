package com.example.loansservices.mapper;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.dto.PaymentDto;
import com.example.loansservices.model.Loans;

public class LoansMapper{
    public static Loans mapToLoans(LoansDto loansDto){
        Loans loans=new Loans();
        loans.setCustomerId(loansDto.getCustomerId());
        loans.setLoanType(loansDto.getLoanType());
        loans.setTotalLoan(loansDto.getTotalLoan());
        loans.setLoanTenureInYears(loansDto.getLoanTenureInYears());
        return loans;
    }

    public static LoansDto mapToLoansDto(Loans loans){
        LoansDto loansDto=new LoansDto();
        loansDto.setLoanNumber(loans.getLoanNumber());
        loansDto.setCustomerId(loans.getCustomerId());
        loansDto.setLoanType(loans.getLoanType());

        loansDto.setTotalLoan(loans.getTotalLoan());
        loansDto.setAmountPaid(loans.getAmountPaid());
        loansDto.setOutstandingAmount(loans.getOutstandingAmount());

        loansDto.setLoanTenureInYears(loans.getLoanTenureInYears());
        loansDto.setEndDt(loans.getEndDt());
        loansDto.setStartDt(loans.getStartDate());

        loansDto.setEmiAmount(loans.getEmiAmount());
        loansDto.setRate_of_Interest(loans.getRate_Of_Interest());
        loansDto.setTotalInstallmentsInNumber(loans.getTotalInstallmentsInNumber());
        loansDto.setInstallmentsPaidInNumber(loans.getInstallmentsPaidInNumber());
        loansDto.setInstallmentsRemainingInNumber(loans.getInstallmentsRemainingInNumber());
        return loansDto;
    }

    public static PaymentDto mapToPaymentDto(Loans loans,Long payment){
        PaymentDto paymentDto=new PaymentDto();
        paymentDto.setLoanNumber(loans.getLoanNumber());
        paymentDto.setCustomerId(loans.getCustomerId());
        paymentDto.setLoanType(loans.getLoanType());
        paymentDto.setTotalLoan(loans.getTotalLoan());
        paymentDto.setPaymentAmount(payment);
        paymentDto.setLoanTenureInYears(loans.getLoanTenureInYears());
        paymentDto.setRate_Of_Interest(loans.getRate_Of_Interest());
        paymentDto.setAmountPaid(loans.getAmountPaid());
        paymentDto.setTotalInstallmentsInNumber(loans.getTotalInstallmentsInNumber());
        paymentDto.setInstallmentsPaidInNumber(loans.getInstallmentsPaidInNumber());
        paymentDto.setInstallmentsRemainingInNumber(loans.getInstallmentsRemainingInNumber());
        paymentDto.setOutstandingAmount(loans.getOutstandingAmount());
        return paymentDto;
    }
}
