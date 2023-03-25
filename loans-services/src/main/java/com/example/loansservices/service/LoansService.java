package com.example.loansservices.service;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.excpetion.TenureException;

public interface LoansService {
    LoansDto borrowLoan(LoansDto loansDto) throws TenureException;
    LoansDto getInfoAboutLoans(Long customerId);
    LoansDto payInstallMents(LoansDto loansDto) throws TenureException;
}
