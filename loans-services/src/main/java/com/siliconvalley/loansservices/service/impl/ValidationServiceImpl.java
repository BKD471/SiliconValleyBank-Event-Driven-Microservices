package com.siliconvalley.loansservices.service.impl;

import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.exception.InstallmentsException;
import com.siliconvalley.loansservices.exception.LoansException;
import com.siliconvalley.loansservices.exception.PaymentException;
import com.siliconvalley.loansservices.exception.ValidationException;
import com.siliconvalley.loansservices.model.Loans;
import com.siliconvalley.loansservices.repository.ILoansRepository;
import com.siliconvalley.loansservices.service.IValidationService;
import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service("validationServicePrimary")
public final class ValidationServiceImpl implements IValidationService {
    private final ILoansRepository loansRepository;

    ValidationServiceImpl(ILoansRepository loansRepository) {
        this.loansRepository = loansRepository;
    }

    /**
     * @param loans
     * @param loansDto
     * @param loansValidateType
     */
    @Override
    public void validator(Loans loans, LoansDto loansDto,
                          AllConstantsHelper.LoansValidateType loansValidateType, Optional<List<Loans>> optionalFields) throws ValidationException, PaymentException, InstallmentsException, LoansException {
        log.debug("<################# validator(Loans, LoansDto," +
                "AllConstantsHelper.LoansValidateType,Optional<List<Loans>>) started " +
                "##################################" +
                "#######>");
        final String methodName = "validator(Loans,LoansDto,LoansValidateType) in ValidationServiceImpl";

        if (Objects.isNull(loansValidateType)) throw new ValidationException(LoansException.class
                , "Please provide a validation type", methodName);
        switch (loansValidateType) {
            case ISSUE_LOAN -> {
                final int MAX_PERMISSIBLE_LOANS = 5;
                final String customerId = loansDto.getCustomerId();
                if (StringUtils.isBlank(customerId)) throw new ValidationException(ValidationException.class
                        , "Please provide customerId", methodName);
                //get credit score

                //get any pre existing running loan
                final Optional<List<Loans>> allActiveLoans =
                        loansRepository.getAllByCustomerIdAndLoanActiveIs(customerId, true);
                if (allActiveLoans.isPresent() && allActiveLoans.get().size() > MAX_PERMISSIBLE_LOANS)
                    throw new ValidationException(ValidationException.class, "You already have too much loans active", methodName);
            }
            case PAY_EMI -> {
                if (optionalFields.isEmpty())
                    throw new LoansException(LoansException.class, String.format("No such loans exist with Loan id %s",
                            loans.getLoanNumber()), methodName);

                BigDecimal payment = loansDto.getPaymentAmount();
                BigDecimal emi = loans.getEmiAmount();
                boolean isLoanClosed = !loans.isLoanActive();

                if (isLoanClosed)
                    throw new InstallmentsException(InstallmentsException.class, String.format("Yr loan with id %s is already closed", loansDto.getLoanNumber()), methodName);

                boolean isInsufficientPayment=new BigDecimal(String.valueOf(payment)).compareTo(emi)<0;
                boolean isPaymentMultiplipleOfEmi=new BigDecimal(String.valueOf(payment)).divide(emi, RoundingMode.FLOOR).intValue()!=0;
                if (isInsufficientPayment || isPaymentMultiplipleOfEmi)
                    throw new PaymentException(PaymentException.class, String.format("yr payment %s should be greater equal to or multiple of yr emi %s", payment, emi), methodName);

            }
            case GET_ALL_LOAN -> {
                if (optionalFields.isEmpty()) throw new LoansException(LoansException.class,
                        String.format("There is no loan found for customer with Id %s", loansDto.getCustomerId()),
                        methodName);
            }
            case GET_INFO_LOAN -> {
                if (optionalFields.isEmpty())
                    throw new LoansException(LoansException.class, String.format("No such loan exist with id %s", loansDto.getCustomerId()), methodName);
            }
        }
        log.debug("<################# validator(Loans, LoansDto," +
                "AllConstantsHelper.LoansValidateType,Optional<List<Loans>>) ended " +
                "##################################" +
                "#######>");
    }
}
