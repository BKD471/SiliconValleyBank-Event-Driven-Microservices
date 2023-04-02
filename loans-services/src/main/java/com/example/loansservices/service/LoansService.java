package com.example.loansservices.service;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.dto.PaymentDto;
import com.example.loansservices.exception.InstallmentsException;
import com.example.loansservices.exception.LoansException;
import com.example.loansservices.exception.PaymentException;
import com.example.loansservices.exception.TenureException;

import java.util.List;

public interface LoansService {
    LoansDto borrowLoan(LoansDto loansDto) throws TenureException;
    LoansDto getInfoAboutLoanByCustomerIdAndLoanNumber(Long customerId,Long LoanNumber) throws  LoansException;
    PaymentDto payInstallments(PaymentDto paymentDto) throws TenureException, PaymentException, InstallmentsException, LoansException;
    List<LoansDto> getAllLoansForCustomerById(Long customerId) throws  LoansException;
}
