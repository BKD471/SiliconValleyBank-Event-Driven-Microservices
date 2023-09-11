package com.siliconvalley.loansservices.service;

import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.exception.InstallmentsException;
import com.siliconvalley.loansservices.exception.LoansException;
import com.siliconvalley.loansservices.exception.PaymentException;
import com.siliconvalley.loansservices.exception.ValidationException;
import com.siliconvalley.loansservices.model.Loans;
import com.siliconvalley.loansservices.helpers.AllConstantsHelper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IValidationService {
    void validator(Loans loans, LoansDto loansDto, AllConstantsHelper.LoansValidateType loansValidateType, Optional<Set<Loans>> optionalField) throws ValidationException, PaymentException, InstallmentsException, LoansException;
}
