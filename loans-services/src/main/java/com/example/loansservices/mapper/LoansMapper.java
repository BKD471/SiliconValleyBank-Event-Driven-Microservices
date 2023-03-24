package com.example.loansservices.mapper;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.model.Loans;

public class LoansMapper{
    public static Loans mapToLoans(LoansDto loansDto){
        Loans loans=new Loans();
        loans.setCustomerId(loansDto.getCustomerId());
        loans.setLoanType(loansDto.getLoanType());
        loans.setTotalLoan(loansDto.getTotalLoan());
        loans.setAmountPaid(loansDto.getAmountPaid());
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

        loansDto.setEndDt(loans.getEndDt());
        loansDto.setStartDt(loans.getStartDate());

        loansDto.setEmiAmount(loans.getEmiAmount());
        loansDto.setRate_of_Interest(loans.getRate_Of_Interest());
        loansDto.setTotalInstallmentsInNumber(loans.getTotalInstallmentsInNumber());
        loansDto.setInstallmentsPaidInNumber(loans.getInstallmentsPaidInNumber());
        loansDto.setInstallmentsRemainingInNumber(loans.getInstallmentsRemainingInNumber());
        return loansDto;
    }


}
