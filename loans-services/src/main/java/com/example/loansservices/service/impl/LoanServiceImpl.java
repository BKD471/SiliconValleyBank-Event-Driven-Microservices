package com.example.loansservices.service.impl;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.dto.PaymentDto;
import com.example.loansservices.exception.InstallmentsException;
import com.example.loansservices.exception.LoansException;
import com.example.loansservices.exception.PaymentException;
import com.example.loansservices.exception.TenureException;
import com.example.loansservices.mapper.LoansMapper;
import com.example.loansservices.model.Loans;
import com.example.loansservices.repository.LoansRepository;
import com.example.loansservices.service.ILoansService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.loansservices.mapper.LoansMapper.mapToLoansDto;
import static com.example.loansservices.utils.RateOfInterestHelper.getRateOfInterest;


/**
 * @parent: LoansService
 * @class: LoanServiceImpl
 * @fields: loansRepository,MONTHS_IN_YEAR,PERCENTAGE
 * @fieldTypes: LoansRepository,int,int
 * @overridenMethods:  borrowLoan,getInfoAboutLoanByCustomerIdAndLoanNumber,
 * payInstallments,getAllLoansForCustomerById
 *@specializedMethods: None
 * */
@Service
@Slf4j
public class LoanServiceImpl implements ILoansService{
    private final int MONTHS_IN_YEAR = 12;
    private final LoansRepository loansRepository;

    /**
     * @param: loansRepository
     * @paramType: LoansRepository
     * @returnType: NA
     * **/
    LoanServiceImpl(LoansRepository loansRepository) {
        this.loansRepository = loansRepository;
    }

    /**
     * Method to calculate monthly emi
     */
    private long calculateEmi(final long loanAmount,final int tenure) throws TenureException {
        final String methodName="calculateEmi(Long,int) in LoanServiceImpl";
        final Double rate_of_interest = getRateOfInterest(tenure);
        if(rate_of_interest==null) throw new TenureException(TenureException.class,
                String.format("Tenure %s is not available",tenure),methodName);

        int PERCENTAGE = 100;
        double magic_coeff = ((rate_of_interest / PERCENTAGE) / MONTHS_IN_YEAR);
        long interest = (long) (loanAmount * magic_coeff);

        double numerator = Math.pow(1 + magic_coeff, tenure * MONTHS_IN_YEAR);
        double denominator = numerator - 1;
        double emi_coeff = (numerator / denominator);

        return (long) (interest * emi_coeff);
    }

    /**
     * Method to process critical information like  no of installments,
     * Maturity Date ,emi amount
     */
    private Loans processLoanInformationAndCreateLoan(final Loans loans) throws TenureException {
        final Loans loan = loansRepository.save(loans);

        //set id
        final String loanNumber= UUID.randomUUID().toString();
        loan.setLoanNumber(loanNumber);

        //setting maturity date & loan tenure
        final int tenure = loan.getLoanTenureInYears();
        loan.setLoanTenureInYears(tenure);
        LocalDate endDate = loan.getStartDate().plusYears(tenure);
        loan.setEndDt(endDate);

        //Calculating emi
        final Long loanAmount = loan.getTotalLoan();
        final Long emiAmount = calculateEmi(loanAmount, tenure);
        loan.setEmiAmount(emiAmount);

        //initializing the initial value for outstanding amount,amount paid,rate of interest
        double rate_of_interest = getRateOfInterest(tenure);
        loan.setRate_Of_Interest(rate_of_interest);
        loan.setOutstandingAmount(loan.getTotalLoan());
        loan.setAmountPaid(0L);

        //Calculating no of installments & loanTenure
        loan.setTotalInstallmentsInNumber(tenure * MONTHS_IN_YEAR);
        loan.setInstallmentsPaidInNumber(0);
        loan.setInstallmentsRemainingInNumber(tenure * MONTHS_IN_YEAR);

        //saving the processed loan
        return loansRepository.save(loan);
    }

    /**
     * @param loansDto
     * @paramType LoansDto
     * @returnType LoansDto
     */
    @Override
    public LoansDto borrowLoan(final LoansDto loansDto) throws TenureException {
        final Loans loan = LoansMapper.mapToLoans(loansDto);
        final Loans processedLoan = processLoanInformationAndCreateLoan(loan);
        final Loans savedLoan = loansRepository.save(processedLoan);
        return mapToLoansDto(savedLoan);
    }

    /**
     * @param paymentDto
     * @paramType PaymentDto
     * @returnType PaymentDto
     */
    @Override
    public PaymentDto payInstallments(final PaymentDto paymentDto) throws PaymentException, InstallmentsException, LoansException {
        final String methodName = "payInstallments(PaymentDto) in LoanServiceImpl";
        final Optional<Loans> loan = loansRepository.findByCustomerIdAndLoanNumber
                (paymentDto.getCustomerId(), paymentDto.getLoanNumber());

        if(loan.isEmpty()) throw  new LoansException(LoansException.class,String.format("No such loans exist with Loan id %s",
                paymentDto.getLoanNumber()),methodName);

        final Loans currentLoan=loan.get();
        long emi = currentLoan.getEmiAmount();
        int paidInstallments = currentLoan.getInstallmentsPaidInNumber();
        int remainingInstallments = currentLoan.getInstallmentsRemainingInNumber();
        if (remainingInstallments <= 0)
            throw new InstallmentsException(InstallmentsException.class,String.format("Yr loan with id %s is already closed",paymentDto.getLoanNumber()), methodName);


        long payment = paymentDto.getPaymentAmount();
        if (payment < emi || (( payment % emi) != 0))
            throw new PaymentException(PaymentException.class,String.format("yr payment %s should be greater equal to or multiple of yr emi %s", payment, emi), methodName);

        //updating the installments data
        int NoOfInstallments = (int) (payment / emi);
        currentLoan.setInstallmentsPaidInNumber(NoOfInstallments + paidInstallments);
        currentLoan.setInstallmentsRemainingInNumber(remainingInstallments - NoOfInstallments);


        //updating the outstanding amount,amount paid
        long outstandingAmount = currentLoan.getOutstandingAmount();
        long amountPaid = currentLoan.getAmountPaid();
        currentLoan.setOutstandingAmount(outstandingAmount - payment);
        currentLoan.setAmountPaid(amountPaid + payment);

        Loans paidEmi = loansRepository.save(currentLoan);
        return LoansMapper.mapToPaymentDto(paidEmi, payment);
    }

    /**
     * @param customerId
     * @paramType Long
     * @returnType List<LoansDto>
     */
    public List<LoansDto> getAllLoansForCustomerById(final String customerId) throws  LoansException{
        final String methodName=" getAllLoansForCustomerById(Long) in LoanServiceImpl";
        final Optional<List<Loans>> allLoans = loansRepository.findAllByCustomerId(customerId);

        if(allLoans.isEmpty()) throw  new LoansException(LoansException.class,
                String.format("There is no loan found for customer with Id %s",customerId),
                methodName);
        return allLoans.get().stream().map(LoansMapper::mapToLoansDto).
                collect(Collectors.toList());
    }

    /**
     * @param customerId
     * @paramType Long
     * @returnType LoansDto
     */
    @Override
    public LoansDto getInfoAboutLoanByCustomerIdAndLoanNumber(final String customerId,final String loanNumber) throws  LoansException{
        final String methodName="getInfoAboutLoanByCustomerIdAndLoanNUmber(Long,Long) in LoanServiceImpl";
        final Optional<Loans> loan = loansRepository.findByCustomerIdAndLoanNumber(customerId, loanNumber);
        if(loan.isEmpty()) throw  new LoansException(LoansException.class,String.format("No such loan exist with id %s",loanNumber),methodName);
        return mapToLoansDto(loan.get());
    }
}
