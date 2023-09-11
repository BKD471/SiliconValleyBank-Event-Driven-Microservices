package com.siliconvalley.loansservices.service;

import com.siliconvalley.loansservices.exception.InstallmentsException;
import com.siliconvalley.loansservices.exception.LoansException;
import com.siliconvalley.loansservices.exception.PaymentException;
import com.siliconvalley.loansservices.exception.ValidationException;
import com.siliconvalley.loansservices.helpers.AllConstantsHelper;

import java.time.LocalDate;

public interface IPdfService {
    void generateStatement(final String customerId,final AllConstantsHelper.FormatType formatType) throws ValidationException, PaymentException, LoansException, InstallmentsException;
}
