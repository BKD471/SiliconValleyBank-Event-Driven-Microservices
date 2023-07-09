package com.example.loansservices.service.impl;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.exception.LoansException;
import com.example.loansservices.exception.ValidationException;
import com.example.loansservices.model.Loans;
import com.example.loansservices.repository.ILoansRepository;
import com.example.loansservices.service.IValidationService;
import com.example.loansservices.utils.AllConstantsHelper;
import org.springframework.stereotype.Service;
import scala.reflect.internal.Positions;

import java.util.Objects;

@Service("validationServicePrimary")
public class ValidationServiceImpl implements IValidationService {
    private final ILoansRepository loansRepository;

    ValidationServiceImpl(ILoansRepository loansRepository){
        this.loansRepository=loansRepository;
    }

    /**
     * @param loans
     * @param loansDto
     * @param loansValidateType
     */
    @Override
    public void validator(Loans loans, LoansDto loansDto,
                          AllConstantsHelper.LoansValidateType loansValidateType) throws ValidationException {
        final String methodName="validator(Loans,LoansDto,LoansValidateType) in ValidationServiceImpl";

        if(Objects.isNull(loansValidateType)) throw  new ValidationException(LoansException.class
                ,"Please provide a validation type",methodName);
        switch (loansValidateType){
            case ISSUE_LOAN -> {

                //get credit score

                //get any pre existing running loan

            }
            case PAY_EMI -> {

            }
            case GET_ALL_LOAN -> {

            }
            case GET_INFO_LOAN -> {

            }
        }
    }
}
