package com.siliconvalley.loansservices.service.impl;

import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.dto.OutPutDto;
import com.siliconvalley.loansservices.helpers.LoansMapperHelper;
import com.siliconvalley.loansservices.model.Emi;
import com.siliconvalley.loansservices.model.Loans;
import com.siliconvalley.loansservices.repository.ILoansRepository;
import com.siliconvalley.loansservices.service.ILoansService;
import com.siliconvalley.loansservices.service.IPdfService;
import com.siliconvalley.loansservices.service.IValidationService;
import com.siliconvalley.loansservices.exception.*;
import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.siliconvalley.loansservices.helpers.AllConstantsHelper.*;
import static com.siliconvalley.loansservices.helpers.AllConstantsHelper.LoansValidateType.GEN_EMI_STMT;
import static com.siliconvalley.loansservices.helpers.LoansMapperHelper.*;
import static com.siliconvalley.loansservices.helpers.RateOfInterestHelper.getRateOfInterest;


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
    private final IPdfService pdfService;

    /**
     * @param: loansRepository
     * @paramType: LoansRepository
     * @returnType: NA
     **/
    LoanServiceImpl(final ILoansRepository loansRepository,
                    final IValidationService validationService,
                    final IPdfService pdfService) {
        this.loansRepository = loansRepository;
        this.validationService = validationService;
        this.pdfService=pdfService;
    }

    /**
     * Method to calculate monthly emi
     */
    private BigDecimal calculateEmi(final BigDecimal loanAmount, final int tenure) throws TenureException {
        log.debug("<#####################calculateEmi(final BigDecimal, final int) in LoanServiceImpl started ###########################################################" +
                "###");
        final String methodName = "calculateEmi(BigDecimal,int) in LoanServiceImpl";
        final Double rate_of_interest = getRateOfInterest(tenure);
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
        final LocalDate endDate = loan.getStartDate().plusYears(tenure);
        final BigDecimal loanAmount = loan.getTotalLoan();
        final BigDecimal emiAmount = calculateEmi(loanAmount, tenure);
        final double rate_of_interest = getRateOfInterest(tenure);

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
        log.debug("<################### borrowLoan(final LoansDto loansDto) started #############################################>");
        final Loans loan = LoansMapperHelper.mapToLoans(loansDto);
        validationService.validator(loan, loansDto, ISSUE_LOAN, Optional.empty());
        final Loans processedLoan = processLoanInformationAndCreateLoan(loan);
        final Loans savedLoan = loansRepository.save(processedLoan);
        log.debug("<################### borrowLoan(final LoansDto loansDto) ended #############################################>");
        return mapToLoansDto(savedLoan);
    }

    /**
     * @param loansDto
     * @paramType PaymentDto
     * @returnType PaymentDto
     */
    private LoansDto payInstallments(final LoansDto loansDto) throws LoansException, PaymentException, InstallmentsException, ValidationException {
        log.debug("<################### payInstallments(final LoansDto loansDto) started #############################################>");
        final Optional<Loans> loan = loansRepository.findByCustomerIdAndLoanNumber
                (loansDto.getCustomerId(), loansDto.getLoanNumber());

        if(loan.isEmpty()) throw new LoansException(LoansException.class,
                String.format("No loans been found with customerId %s & loanNumber %s"
                ,loansDto.getCustomerId(),loansDto.getLoanNumber()),"payInstallments(LoansDto)");

        LoansDto processedLoansDto= mapToLoansDto(loan.get());
        processedLoansDto.setPaymentAmount(loansDto.getPaymentAmount());
        validationService.validator(loan.get(), processedLoansDto, PAY_EMI,Optional.of(Set.of(loan.get())));

        final Loans currentLoan = loan.get();
        final BigDecimal emi = currentLoan.getEmiAmount();
        final int paidInstallments = currentLoan.getInstallmentsPaidInNumber();
        final int remainingInstallments = currentLoan.getInstallmentsRemainingInNumber();
        final BigDecimal payment = processedLoansDto.getPaymentAmount();

        final int NoOfInstallments =new BigDecimal(String.valueOf(payment)).divide(emi, RoundingMode.CEILING).intValue();
        currentLoan.setInstallmentsPaidInNumber(NoOfInstallments + paidInstallments);
        currentLoan.setInstallmentsRemainingInNumber(remainingInstallments - NoOfInstallments);
        if(currentLoan.getInstallmentsRemainingInNumber()==0) currentLoan.setLoanActive(false);

        final BigDecimal outstandingAmount = currentLoan.getOutstandingAmount();
        final BigDecimal amountPaid = currentLoan.getAmountPaid();
        final BigDecimal outStandingAmountToBePaid=new BigDecimal(String.valueOf(outstandingAmount)).subtract(payment);
        currentLoan.setOutstandingAmount(outStandingAmountToBePaid);
        final BigDecimal amountToBePaid=new BigDecimal(String.valueOf(amountPaid)).add(payment);
        currentLoan.setAmountPaid(amountToBePaid);

        final String emiId=UUID.randomUUID().toString();
        Emi emiss= Emi.builder()
                   .emiId(emiId)
                .emiAmount(payment)
                .amountPaid(amountPaid)
                .outStandingAMount(outStandingAmountToBePaid)
                .timeStamp(LocalDateTime.now()).build();

        Set<Emi> setOfEmis=new HashSet<>();
        setOfEmis.add(emiss);
        currentLoan.setSetOfEmis(setOfEmis);
        emiss.setLoans(currentLoan);

        final Loans paidEmi = loansRepository.save(currentLoan);
        log.debug("<################### payInstallments(final LoansDto loansDto) ended #############################################>");
        return mapToPaymentDto(paidEmi, payment);
    }

    /**
     * @param customerId
     * @paramType Long
     * @returnType LoansDto
     */
    private LoansDto getLoanInfoByCustomerIdAndLoanNumber(final String customerId, final String loanNumber) throws LoansException, ValidationException, PaymentException, InstallmentsException {
        log.debug("<################### getLoanInfoByCUstomerIdAndLoanNumber(final LoansDto loansDto) started #############################################>");
        final Optional<Loans> loan = loansRepository.findByCustomerIdAndLoanNumber(customerId, loanNumber);
        validationService.validator(loan.get(),null, GET_INFO_LOAN ,Optional.of(Set.of(loan.get())));
        log.debug("<################### getLoanInfoByCustomerIdAndLoanNumber(final LoansDto loansDto) ended #############################################>");
        return mapToLoansDto(loan.get());
    }


    /**
     * @param customerId
     * @paramType Long
     * @returnType List<LoansDto>
     */
    public Set<LoansDto> getAllLoansByCustomerId(final String customerId) throws LoansException, ValidationException, PaymentException, InstallmentsException {
        log.debug("<################### getAllLoansByCustomerId(String CustomerId) started #############################################>");
        final Optional<Set<Loans>> allLoans = loansRepository.findAllByCustomerId(customerId);
        validationService.validator(null,LoansDto.builder().customerId(customerId).build(), GET_ALL_LOAN,allLoans);
        log.debug("<################### getAllLoansByCustomerId(String customerId) ended #############################################>");
        return allLoans.get().stream().map(LoansMapperHelper::mapToLoansDto).
                collect(Collectors.toSet());
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     */
    public void downloadAllEmiStatements(final LocalDate startDate,final LocalDate endDate,
                                         final String customerId,final String loanNumber,
                                         final FormatType formatType) throws ValidationException, PaymentException, LoansException, InstallmentsException {
        Optional<Loans> fetchedLoan=loansRepository.findByCustomerIdAndLoanNumber(customerId,loanNumber);

        validationService.validator(fetchedLoan.get(),null,GEN_EMI_STMT,Optional.of(Set.of(fetchedLoan.get())));

        List<Emi> listOfEmis=fetchedLoan.get().getSetOfEmis().stream().toList();
        LocalDate tempStartDate=startDate;
        LocalDate tempEndDate=endDate;
        if(Objects.isNull(tempStartDate)) tempStartDate=listOfEmis.get(0).getTimeStamp().toLocalDate();
        if(Objects.isNull(tempEndDate)) tempEndDate=LocalDate.now();
        pdfService.generateStatement(fetchedLoan.get(),listOfEmis,startDate,endDate,formatType);
    }


    /**
     * @param loansDto
     * @return
     */
    @Override
    public OutPutDto loansExecutor(LoansDto loansDto) throws LoansException, ValidationException, PaymentException, InstallmentsException, TenureException {
        validationService.validator(mapToLoans(loansDto),loansDto, DRIVER_METHOD_VALIDATION, Optional.empty());
        final String customerId=loansDto.getCustomerId();
        final String loanNumber= loansDto.getLoanNumber();

        final LocalDate startDate=loansDto.getStartDt();
        final LocalDate endDate=loansDto.getEndDt();
        final FormatType formatType=loansDto.getFormatType();

        AllConstantsHelper.RequestType requestType=loansDto.getRequestType();
        switch (requestType){
            case BORROW_LOAN -> {
                final LoansDto responseDto=borrowLoan(loansDto);
                return OutPutDto.builder().loansDto(responseDto).build();
            }
            case PAY_INSTALLMENTS -> {
               final LoansDto responseDto=payInstallments(loansDto);
               return OutPutDto.builder().loansDto(responseDto).build();
            }
            case GET_INFO -> {
                validationService.validator(mapToLoans(loansDto),loansDto,
                        DRIVER_METHOD_VALIDATION,Optional.empty());
                final LoansDto responseDto=getLoanInfoByCustomerIdAndLoanNumber(customerId,loanNumber);
                return OutPutDto.builder().loansDto(responseDto).build();
            }
            case GET_ALL_LOANS_FOR_CUSTOMER -> {
               final Set<LoansDto> loansSet=getAllLoansByCustomerId(customerId);
               return OutPutDto.builder().listOfLoans(loansSet).build();
            }
            case DOWNLOAD_EMI_STMT -> {
                downloadAllEmiStatements(startDate,endDate,customerId,loanNumber,formatType);
                return OutPutDto.builder().defaultMessage(String.format("Emi Stmt generated for customer with id %s",customerId)).build();
            }
            default -> throw  new BadApiRequestException(BadApiRequestException.class,"Invalid request type","transactionsExecutor(LoansDto loansDto)");
        }
    }
}
