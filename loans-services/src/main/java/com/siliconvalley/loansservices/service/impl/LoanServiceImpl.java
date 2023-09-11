package com.siliconvalley.loansservices.service.impl;

import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.dto.OutPutDto;
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

import static com.siliconvalley.loansservices.helpers.AllConstantsHelper.*;
import static com.siliconvalley.loansservices.helpers.LoansMapperHelper.*;


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
    private LoansDto borrowLoan(final LoansDto loansDto) throws TenureException, ValidationException, PaymentException, InstallmentsException, LoansException {
        final Loans loan = LoansMapperHelper.mapToLoans(loansDto);
        validationService.validator(loan, loansDto, AllConstantsHelper.ISSUE_LOAN, Optional.empty());
        final Loans processedLoan = processLoanInformationAndCreateLoan(loan);
        final Loans savedLoan = loansRepository.save(processedLoan);
        return mapToLoansDto(savedLoan);
    }

    /**
     * @param loansDto
     * @paramType PaymentDto
     * @returnType PaymentDto
     */
    private LoansDto payInstallments(final LoansDto loansDto) throws LoansException, PaymentException, InstallmentsException, ValidationException {
        final Optional<Loans> loan = loansRepository.findByCustomerIdAndLoanNumber
                (loansDto.getCustomerId(), loansDto.getLoanNumber());

        if(loan.isEmpty()) throw new LoansException(LoansException.class,
                String.format("No loans been found with customerId %s & loanNumber %s"
                ,loansDto.getCustomerId(),loansDto.getLoanNumber()),"payInstallments(LoansDto)");

        LoansDto processedLoansDto= mapToLoansDto(loan.get());
        processedLoansDto.setPaymentAmount(loansDto.getPaymentAmount());
        validationService.validator(loan.get(), processedLoansDto, PAY_EMI,Optional.of(List.of(loan.get())));

        final Loans currentLoan = loan.get();
        BigDecimal emi = currentLoan.getEmiAmount();
        int paidInstallments = currentLoan.getInstallmentsPaidInNumber();
        int remainingInstallments = currentLoan.getInstallmentsRemainingInNumber();
        BigDecimal payment = processedLoansDto.getPaymentAmount();

        int NoOfInstallments =new BigDecimal(String.valueOf(payment)).divide(emi, RoundingMode.CEILING).intValue();
        currentLoan.setInstallmentsPaidInNumber(NoOfInstallments + paidInstallments);
        currentLoan.setInstallmentsRemainingInNumber(remainingInstallments - NoOfInstallments);
        if(currentLoan.getInstallmentsRemainingInNumber()==0) currentLoan.setLoanActive(false);

        BigDecimal outstandingAmount = currentLoan.getOutstandingAmount();
        BigDecimal amountPaid = currentLoan.getAmountPaid();
        BigDecimal outStandingAmountToBePaid=new BigDecimal(String.valueOf(outstandingAmount)).subtract(payment);
        currentLoan.setOutstandingAmount(outStandingAmountToBePaid);
        BigDecimal amountToBePaid=new BigDecimal(String.valueOf(amountPaid)).add(payment);
        currentLoan.setAmountPaid(amountToBePaid);

        Loans paidEmi = loansRepository.save(currentLoan);
        return mapToPaymentDto(paidEmi, payment);
    }

    /**
     * @param customerId
     * @paramType Long
     * @returnType LoansDto
     */
    private LoansDto getInfoAboutAParticularLoan(final String customerId, final String loanNumber) throws LoansException, ValidationException, PaymentException, InstallmentsException {
        final Optional<Loans> loan = loansRepository.findByCustomerIdAndLoanNumber(customerId, loanNumber);
        validationService.validator(loan.get(),null, GET_INFO_LOAN ,Optional.of(List.of(loan.get())));
        return mapToLoansDto(loan.get());
    }


    /**
     * @param customerId
     * @paramType Long
     * @returnType List<LoansDto>
     */
    public List<LoansDto> getAllLoansForACustomer(final String customerId) throws LoansException, ValidationException, PaymentException, InstallmentsException {
        final Optional<List<Loans>> allLoans = loansRepository.findAllByCustomerId(customerId);
        validationService.validator(null,LoansDto.builder().customerId(customerId).build(), AllConstantsHelper.GET_ALL_LOAN,allLoans);
        return allLoans.get().stream().map(LoansMapperHelper::mapToLoansDto).
                collect(Collectors.toList());
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     */
    public LoansDto downloadAllEmiStatements(final LocalDate startDate,final LocalDate endDate,final String customerId) {
        return null;
    }


    /**
     * @param loansDto
     * @return
     */
    @Override
    public OutPutDto transactionsExecutor(LoansDto loansDto) throws LoansException, ValidationException, PaymentException, InstallmentsException, TenureException {
        validationService.validator(mapToLoans(loansDto),loansDto, DRIVER_METHOD_VALIDATION, Optional.empty());
        final String customerId=loansDto.getCustomerId();
        final String loanNumber= loansDto.getLoanNumber();

        final LocalDate startDate=loansDto.getStartDt();
        final LocalDate endDate=loansDto.getEndDt();

        AllConstantsHelper.RequestType requestType=loansDto.getRequestType();
        switch (requestType){
            case BORROW_LOAN -> {
                LoansDto responseDto=borrowLoan(loansDto);
                return OutPutDto.builder().loansDto(responseDto).build();
            }
            case PAY_INSTALLMENTS -> {
               LoansDto responseDto=payInstallments(loansDto);
               return OutPutDto.builder().loansDto(responseDto).build();
            }
            case GET_INFO -> {
                validationService.validator(mapToLoans(loansDto),loansDto,
                        DRIVER_METHOD_VALIDATION,Optional.empty());
                LoansDto responseDto=getInfoAboutAParticularLoan(customerId,loanNumber);
                return OutPutDto.builder().loansDto(responseDto).build();
            }
            case GET_ALL_LOANS_FOR_CUSTOMER -> {
               List<LoansDto> loansList=getAllLoansForACustomer(customerId);
               return OutPutDto.builder().listOfLoans(loansList).build();
            }
            case DOWNLOAD_EMI_STMT -> {
                LoansDto loansDtos=downloadAllEmiStatements(startDate,endDate,customerId);
                return OutPutDto.builder().loansDto(loansDtos).build();
            }
        }
        return null;
    }
}
