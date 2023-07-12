package com.example.loansservices.service.impl;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.exception.InstallmentsException;
import com.example.loansservices.exception.LoansException;
import com.example.loansservices.exception.PaymentException;
import com.example.loansservices.exception.ValidationException;
import com.example.loansservices.model.Loans;
import com.example.loansservices.repository.ILoansRepository;
import com.example.loansservices.service.IValidationService;
import com.example.loansservices.helpers.AllConstantsHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Objects;
import java.util.Optional;

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
                          AllConstantsHelper.LoansValidateType loansValidateType,Optional<Loans> optionalFields,Optional<List<Loans>> listOptional) throws ValidationException, PaymentException, InstallmentsException, LoansException {
        final String methodName="validator(Loans,LoansDto,LoansValidateType) in ValidationServiceImpl";

        if(Objects.isNull(loansValidateType)) throw  new ValidationException(LoansException.class
                ,"Please provide a validation type",methodName);
        switch (loansValidateType){
            case ISSUE_LOAN -> {
                final int MAX_PERMISSIBLE_LOANS=5;
                final String customerId=loansDto.getCustomerId();
                if(StringUtils.isBlank(customerId)) throw new ValidationException(ValidationException.class
                        ,"Please provide customerId",methodName);
                //get credit score

                //get any pre existing running loan
                final Optional<List<Loans>> allActiveLoans=
                        loansRepository.getAllByCustomerIdAndLoanActiveIs(customerId,true);
                if(allActiveLoans.isPresent() && allActiveLoans.get().size()>MAX_PERMISSIBLE_LOANS)
                    throw new ValidationException(ValidationException.class,"You already have too much loans active",methodName);
            }
            case PAY_EMI -> {
                if (optionalFields.isEmpty())
                    throw new LoansException(LoansException.class, String.format("No such loans exist with Loan id %s",
                            loans.getLoanNumber()), methodName);

                int payment=loansDto.getInstallmentsRemainingInNumber();
                long emi=loans.getEmiAmount();
                boolean isLoanClosed=!loans.isLoanActive();

                if (isLoanClosed) throw new InstallmentsException(InstallmentsException.class,String.format("Yr loan with id %s is already closed",loansDto.getLoanNumber()), methodName);
                if (payment < emi || (( payment % emi) != 0))
                    throw new PaymentException(PaymentException.class,String.format("yr payment %s should be greater equal to or multiple of yr emi %s", payment, emi), methodName);

            }
            case GET_ALL_LOAN -> {
                if(listOptional.isEmpty()) throw  new LoansException(LoansException.class,
                        String.format("There is no loan found for customer with Id %s",loansDto.getCustomerId()),
                        methodName);
            }
            case GET_INFO_LOAN -> {
                if (optionalFields.isEmpty())
                    throw new LoansException(LoansException.class, String.format("No such loan exist with id %s", loansDto.getCustomerId()), methodName);
            }
        }
    }
}
