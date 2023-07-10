package com.example.loansservices.service;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.dto.PaymentDto;
import com.example.loansservices.exception.*;

import java.util.List;

public interface ILoansService {
    LoansDto borrowLoan(final LoansDto loansDto) throws TenureException, ValidationException, PaymentException, InstallmentsException, LoansException;
    LoansDto getInfoAboutAParticularLoan(final String customerId,final String LoanNumber) throws LoansException, ValidationException, PaymentException, InstallmentsException;
    PaymentDto payInstallments(final PaymentDto paymentDto) throws TenureException, PaymentException, InstallmentsException, LoansException, ValidationException;
    List<LoansDto> getAllLoansForACustomer(final String customerId) throws LoansException, ValidationException, PaymentException, InstallmentsException;
}
