package com.siliconvalley.loansservices.service;

import com.siliconvalley.loansservices.exception.InstallmentsException;
import com.siliconvalley.loansservices.exception.LoansException;
import com.siliconvalley.loansservices.exception.PaymentException;
import com.siliconvalley.loansservices.exception.ValidationException;
import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import com.siliconvalley.loansservices.model.Emi;
import com.siliconvalley.loansservices.model.Loans;

import java.time.LocalDate;
import java.util.List;

public interface IPdfService {
    void generateStatement(final Loans loans, final List<Emi> emis, final LocalDate startDate, final LocalDate endDate, final AllConstantsHelper.FormatType formatType) throws ValidationException, PaymentException, LoansException, InstallmentsException;
}
