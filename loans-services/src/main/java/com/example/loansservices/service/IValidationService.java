package com.example.loansservices.service;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.exception.ValidationException;
import com.example.loansservices.model.Loans;
import com.example.loansservices.utils.AllConstantsHelper;
import scala.reflect.internal.Positions;

public interface IValidationService {
    void validator(Loans loans, LoansDto loansDto, AllConstantsHelper.LoansValidateType loansValidateType) throws ValidationException;
}
