package com.siliconvalley.loansservices.service.impl;

import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.exception.*;
import com.siliconvalley.loansservices.exception.builders.ExceptionBuilder;
import com.siliconvalley.loansservices.helpers.TriPredicateCustom;
import com.siliconvalley.loansservices.model.Loans;
import com.siliconvalley.loansservices.repository.ILoansRepository;
import com.siliconvalley.loansservices.service.IValidationService;
import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.siliconvalley.loansservices.helpers.AllConstantsHelper.ExceptionCodes.*;


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
                          AllConstantsHelper.LoansValidateType loansValidateType, Optional<Set<Loans>> optionalFields) throws ValidationException, PaymentException, InstallmentsException, LoansException {
        log.debug("<################# validator(Loans, LoansDto," +
                "AllConstantsHelper.LoansValidateType,Optional<List<Loans>>) started " +
                "##################################" +
                "#######>");
        final String methodName = "validator(Loans,LoansDto,LoansValidateType) in ValidationServiceImpl";
        final MathContext mc=MathContext.DECIMAL128;
        if (Objects.isNull(loansValidateType)) throw new ValidationException(LoansException.class
                , "Please provide a validation type", methodName);
        switch (loansValidateType) {
            case ISSUE_LOAN -> {
                final int MAX_PERMISSIBLE_LOANS = 5;
                final String customerId = loansDto.getCustomerId();



                if (StringUtils.isBlank(customerId)) throw (ValidationException) ExceptionBuilder.builder()
                        .className(ValidationException.class)
                        .reason("Please provide customerId")
                        .methodName(methodName)
                        .build(VALIDATION_EXEC);
                //get credit score

                //get any pre existing running loan
                final Optional<Set<Loans>> allActiveLoans =loansRepository.getAllByCustomerIdForActiveOrInactiveLoans(customerId, true);
                if (allActiveLoans.isPresent() && allActiveLoans.get().size() > MAX_PERMISSIBLE_LOANS)
                    throw (LoansException) ExceptionBuilder.builder().className(LoansException.class)
                            .reason("You already have too much loans active")
                            .methodName(methodName)
                            .build(LOAN_EXEC);
            }
            case PAY_EMI -> {
                if (optionalFields.isEmpty())
                    throw (ValidationException) ExceptionBuilder.builder().className(ValidationException.class)
                        .reason(String.format("No such loans exist with Loan id %s",
                                loans.getLoanNumber()))
                        .methodName(methodName)
                        .build(VALIDATION_EXEC);

                boolean isLoanClosed = !loans.isLoanActive();
                if (isLoanClosed)
                    throw (InstallmentsException) ExceptionBuilder.builder().className(InstallmentsException.class)
                        .reason(String.format("Yr loan with id %s is already closed",
                                loansDto.getLoanNumber()))
                        .methodName(methodName)
                        .build(INSTALLMENT_EXEC);

                BiPredicate<LoansDto,Loans> guardClauseForPaymentAcceptanceCriteria=(dto, entity)->
                        new BigDecimal(String.valueOf(dto.getPaymentAmount()),mc).compareTo(entity.getEmiAmount())<0
                        || new BigDecimal(String.valueOf(dto.getPaymentAmount()),mc)
                                .divide(entity.getEmiAmount(), RoundingMode.UNNECESSARY)
                                .remainder(BigDecimal.valueOf(1),mc).compareTo(BigDecimal.ZERO)!=0;

                if (guardClauseForPaymentAcceptanceCriteria.test(loansDto,loans))
                    throw (PaymentException) ExceptionBuilder.builder().className(PaymentException.class)
                            .reason(String.format("yr payment %s should be greater equal to or multiple of yr emi %s",
                                    loansDto.getPaymentAmount(), loans.getEmiAmount()))
                            .methodName(methodName)
                            .build(PAYMENT_EXEC);;

            }
            case GET_ALL_LOAN -> {
                if (optionalFields.isEmpty()) throw (ValidationException) ExceptionBuilder.builder().className(ValidationException.class)
                        .reason(String.format("There is no loan found for customer with Id %s", loansDto.getCustomerId()))
                        .methodName(methodName)
                        .build(VALIDATION_EXEC);
            }
            case GET_INFO_LOAN -> {
                if (optionalFields.isEmpty()) throw (LoansException) ExceptionBuilder.builder().className(LoansException.class)
                            .reason(String.format("No such loan exist with id %s", loansDto.getCustomerId()))
                            .methodName(methodName)
                            .build(LOAN_EXEC);
            }
            case GEN_EMI_STMT -> {
                if (optionalFields.isEmpty()) throw (ValidationException) ExceptionBuilder.builder().className(ValidationException.class)
                        .reason("No loans persent for customer WIth Id")
                        .methodName(methodName)
                        .build(VALIDATION_EXEC);
            }
            case DRIVER_METHOD_VALIDATION -> {
                final AllConstantsHelper.RequestType requestType= loansDto.getRequestType();
                final String customerId=loansDto.getCustomerId();
                final String loanNumber= loansDto.getLoanNumber();
                final LocalDate startDate=loansDto.getStartDt();
                final LocalDate endDate=loansDto.getEndDt();

                if(Objects.isNull(requestType)) throw (LoansException) ExceptionBuilder.builder().className(LoansException.class)
                        .reason("Please provide request Type")
                        .methodName(methodName)
                        .build(LOAN_EXEC);

                Predicate<String> testCustomerIdNotNull=StringUtils::isEmpty;
                Predicate<LocalDate> testDateNotNull=Objects::isNull;

                BiPredicate<String,String> testGetInfo= (s1,s2)-> testCustomerIdNotNull.test(s1) || StringUtils.isEmpty(s2);

                TriPredicateCustom<String,LocalDate,LocalDate> testDownloadEmiStmt=(s1,d1,d2)-> testCustomerIdNotNull.test(s1)
                        || testDateNotNull.test(d1)
                        || testDateNotNull.test(d2);

                switch (requestType){
                    case GET_INFO -> {
                        if(testGetInfo.test(customerId,loanNumber)) throw (LoansException) ExceptionBuilder.builder().className(LoansException.class)
                                .reason("Please provide customerId or loanNumber")
                                .methodName(methodName)
                                .build(LOAN_EXEC);;
                    }
                    case GET_ALL_LOANS_FOR_CUSTOMER -> {
                        if(testCustomerIdNotNull.test(customerId))  throw (LoansException) ExceptionBuilder.builder().className(LoansException.class)
                                .reason("Please provide customerId")
                                .methodName(methodName)
                                .build(LOAN_EXEC);
                    }
                    case DOWNLOAD_EMI_STMT -> {
                        if(testDownloadEmiStmt.test(customerId,startDate,endDate)) throw (LoansException) ExceptionBuilder.builder().className(LoansException.class)
                                .reason("Please provide startDate or endDate or customerId")
                                .methodName(methodName)
                                .build(LOAN_EXEC);
                    }
                }
            }
        }
        log.debug("<################# validator(Loans, LoansDto," +
                "AllConstantsHelper.LoansValidateType,Optional<List<Loans>>) ended " +
                "##################################" +
                "#######>");
    }
}
