package com.example.loansservices.service;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.dto.PaymentDto;
import com.example.loansservices.exception.InstallmentsException;
import com.example.loansservices.exception.PaymentException;
import com.example.loansservices.exception.TenureException;

import java.util.List;

public interface LoansService {
    LoansDto borrowLoan(LoansDto loansDto) throws TenureException;
    LoansDto getInfoAboutLoanByCustomerIdAndLoanNumber(Long customerId,Long LoanNumber);
    PaymentDto payInstallments(PaymentDto paymentDto) throws TenureException, PaymentException, InstallmentsException;
    List<LoansDto> getAllLoansForCustomerById(Long customerId);
}
