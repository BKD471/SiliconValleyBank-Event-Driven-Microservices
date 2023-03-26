package com.example.loansservices.service;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.dto.PaymentDto;
import com.example.loansservices.exception.InstallmentsException;
import com.example.loansservices.exception.PaymentException;
import com.example.loansservices.exception.TenureException;

public interface LoansService {
    LoansDto borrowLoan(LoansDto loansDto) throws TenureException;
    LoansDto getInfoAboutLoans(Long customerId);
    PaymentDto payInstallments(PaymentDto paymentDto) throws TenureException, PaymentException, InstallmentsException;
}
