package com.siliconvalley.loansservices.service.impl;

import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.dto.PaymentDto;
import com.example.loansservices.exception.*;
import com.siliconvalley.loansservices.helpers.LoansMapperHelper;
import com.siliconvalley.loansservices.model.Loans;
import com.siliconvalley.loansservices.repository.ILoansRepository;
import com.siliconvalley.loansservices.service.ILoansService;
import com.siliconvalley.loansservices.service.IValidationService;
import com.siliconvalley.loansservices.exception.*;
import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import com.siliconvalley.loansservices.helpers.RateOfInterestHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @parent: LoansService
 * @class: LoanServiceImpl
 * @fields: loansRepository, MONTHS_IN_YEAR, PERCENTAGE
 * @fieldTypes: LoansRepository, int, int
 * @overridenMethods: borrowLoan, getInfoAboutLoanByCustomerIdAndLoanNumber,
 * payInstallments,getAllLoansForCustomerById
 * @specializedMethods: None
 */
@Service
@Slf4j
public class LoanServiceImpl implements ILoansService {
    private final IValidationService validationService;
    private final ILoansRepository loansRepository;

    /**
     * @param: loansRepository
     * @paramType: LoansRepository
     * @returnType: NA
     **/
    LoanServiceImpl(final ILoansRepository loansRepository,final IValidationService validationService) {
        this.loansRepository = loansRepository;
        this.validationService = validationService;
    }

    /**
     * Method to calculate monthly emi
     */
    private BigDecimal calculateEmi(final BigDecimal loanAmount, final int tenure) throws TenureException {
        log.debug("<#####################calculateEmi(final BigDecimal, final int) in LoanServiceImpl started ###########################################################" +
                "###");
        final String methodName = "calculateEmi(Long,int) in LoanServiceImpl";
        final Double rate_of_interest = RateOfInterestHelper.getRateOfInterest(tenure);
        if (Objects.isNull(rate_of_interest)) throw new TenureException(TenureException.class,
                String.format("Tenure %s is not available", tenure), methodName);

        final BigDecimal magic_co_eff = BigDecimal.valueOf(((rate_of_interest / 100) / 12));
        final BigDecimal interest =  new BigDecimal(String.valueOf(loanAmount)).multiply(magic_co_eff);

        final BigDecimal numerator = new BigDecimal(String.valueOf(new BigDecimal(String.valueOf(magic_co_eff)).add(BigDecimal.valueOf(1)))).pow(tenure*12);
        final BigDecimal denominator = new BigDecimal(String.valueOf(numerator)).subtract(BigDecimal.valueOf(1));
        final BigDecimal emi_co_eff = new BigDecimal(String.valueOf(numerator)).divide(denominator,RoundingMode.FLOOR);
        log.debug("<##################### calculateEmi(final BigDecimal, final int) in LoanServiceImpl ended ###########################################################" +
                "###");
        return new BigDecimal(String.valueOf(interest)) .multiply( emi_co_eff);
    }

    /**
     * Method to process critical information like  no of installments,
     * Maturity Date ,emi amount
     */
    private Loans processLoanInformationAndCreateLoan(final Loans loans) throws TenureException {
        log.debug("<############ processLoanInformationAndCreateLoan(final Loans) started ###############" +
                "#########################>");
        final Loans loan = loansRepository.save(loans);
        final String loanNumber = UUID.randomUUID().toString();
        final int tenure = loan.getLoanTenureInYears();
        LocalDate endDate = loan.getStartDate().plusYears(tenure);
        final BigDecimal loanAmount = loan.getTotalLoan();
        final BigDecimal emiAmount = calculateEmi(loanAmount, tenure);
        final double rate_of_interest = RateOfInterestHelper.getRateOfInterest(tenure);

        loan.setLoanNumber(loanNumber);
        loan.setLoanTenureInYears(tenure);
        loan.setEndDt(endDate);
        loan.setEmiAmount(emiAmount);
        loan.setRate_Of_Interest(rate_of_interest);
        loan.setOutstandingAmount(loan.getTotalLoan());
        loan.setAmountPaid(BigDecimal.valueOf(0L));
        loan.setTotalInstallmentsInNumber(tenure * 12);
        loan.setInstallmentsPaidInNumber(0);
        loan.setInstallmentsRemainingInNumber(tenure * 12);
        loan.setLoanActive(true);
        log.debug("<############ processLoanInformationAndCreateLoan(final Loans) ended ###############" +
                "#########################>");
        return loan;
    }

    /**
     * @param loansDto
     * @paramType LoansDto
     * @returnType LoansDto
     */
    @Override
    public LoansDto borrowLoan(final LoansDto loansDto) throws TenureException, ValidationException, PaymentException, InstallmentsException, LoansException {
        final Loans loan = LoansMapperHelper.mapToLoans(loansDto);
        validationService.validator(loan, loansDto, AllConstantsHelper.ISSUE_LOAN, Optional.empty());
        final Loans processedLoan = processLoanInformationAndCreateLoan(loan);
        final Loans savedLoan = loansRepository.save(processedLoan);
        return LoansMapperHelper.mapToLoansDto(savedLoan);
    }

    /**
     * @param paymentDto
     * @paramType PaymentDto
     * @returnType PaymentDto
     */
    @Override
    public PaymentDto payInstallments(final PaymentDto paymentDto) throws LoansException, PaymentException, InstallmentsException, ValidationException {
        final Optional<Loans> loan = loansRepository.findByCustomerIdAndLoanNumber
                (paymentDto.getCustomerId(), paymentDto.getLoanNumber());

        LoansDto loansDto= LoansMapperHelper.mapToLoansDto(loan.get());
        loansDto.setPaymentAmount(paymentDto.getPaymentAmount());
        validationService.validator(loan.get(), loansDto, AllConstantsHelper.PAY_EMI,Optional.of(List.of(loan.get())));

        final Loans currentLoan = loan.get();
        BigDecimal emi = currentLoan.getEmiAmount();
        int paidInstallments = currentLoan.getInstallmentsPaidInNumber();
        int remainingInstallments = currentLoan.getInstallmentsRemainingInNumber();
        BigDecimal payment = paymentDto.getPaymentAmount();

        //updating the installments data
        int NoOfInstallments =new BigDecimal(String.valueOf(payment)).divide(emi, RoundingMode.CEILING).intValue();
        currentLoan.setInstallmentsPaidInNumber(NoOfInstallments + paidInstallments);
        currentLoan.setInstallmentsRemainingInNumber(remainingInstallments - NoOfInstallments);
        if(currentLoan.getInstallmentsRemainingInNumber()==0) currentLoan.setLoanActive(false);

        //updating the outstanding amount,amount paid
        BigDecimal outstandingAmount = currentLoan.getOutstandingAmount();
        BigDecimal amountPaid = currentLoan.getAmountPaid();
        BigDecimal outStandingAmountToBePaid=new BigDecimal(String.valueOf(outstandingAmount)).subtract(payment);
        currentLoan.setOutstandingAmount(outStandingAmountToBePaid);
        BigDecimal amountToBePaid=new BigDecimal(String.valueOf(amountPaid)).add(payment);
        currentLoan.setAmountPaid(amountToBePaid);

        Loans paidEmi = loansRepository.save(currentLoan);
        return LoansMapperHelper.mapToPaymentDto(paidEmi, payment);
    }

    /**
     * @param customerId
     * @paramType Long
     * @returnType LoansDto
     */
    @Override
    public LoansDto getInfoAboutAParticularLoan(final String customerId, final String loanNumber) throws LoansException, ValidationException, PaymentException, InstallmentsException {
        final Optional<Loans> loan = loansRepository.findByCustomerIdAndLoanNumber(customerId, loanNumber);
        validationService.validator(loan.get(),null, AllConstantsHelper.LoansValidateType.GET_INFO_LOAN ,Optional.of(List.of(loan.get())));
        return LoansMapperHelper.mapToLoansDto(loan.get());
    }


    /**
     * @param customerId
     * @paramType Long
     * @returnType List<LoansDto>
     */
    @Override
    public List<LoansDto> getAllLoansForACustomer(final String customerId) throws LoansException, ValidationException, PaymentException, InstallmentsException {
        final Optional<List<Loans>> allLoans = loansRepository.findAllByCustomerId(customerId);
        validationService.validator(null,LoansDto.builder().customerId(customerId).build(), AllConstantsHelper.GET_ALL_LOAN,allLoans);
        return allLoans.get().stream().map(LoansMapperHelper::mapToLoansDto).
                collect(Collectors.toList());
    }
}
