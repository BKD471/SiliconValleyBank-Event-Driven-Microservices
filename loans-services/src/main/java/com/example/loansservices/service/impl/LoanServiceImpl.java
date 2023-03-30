package com.example.loansservices.service.impl;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.dto.PaymentDto;
import com.example.loansservices.exception.InstallmentsException;
import com.example.loansservices.exception.PaymentException;
import com.example.loansservices.exception.TenureException;
import com.example.loansservices.mapper.LoansMapper;
import com.example.loansservices.model.Loans;
import com.example.loansservices.repository.LoansRepository;
import com.example.loansservices.service.LoansService;
import com.example.loansservices.utils.RateOfInterestHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;




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
public class LoanServiceImpl implements LoansService {
    private final int MONTHS_IN_YEAR = 12;
    private final int PERCENTAGE = 100;
    private LoansRepository loansRepository;

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
    private Long calculateEmi(Long loanAmount, int tenure) throws TenureException {
        String methodName="calculateEmi() in LoanServiceImpl";
        Double rate_of_interest = RateOfInterestHelper.getRateOfInterest(tenure);
        if(rate_of_interest==null) throw new TenureException(String.format("Tenure %s is not available",tenure),methodName);

        Double magic_coeff = ((rate_of_interest / PERCENTAGE) / MONTHS_IN_YEAR);
        Long interest = (long) (loanAmount * magic_coeff);

        Double numerator = Math.pow(1 + magic_coeff, tenure * MONTHS_IN_YEAR);
        Double denominator = numerator - 1;
        Double emi_coeff = (numerator / denominator);

        return (long) (interest * emi_coeff);
    }

    /**
     * Method to process critical information like  no of installments,
     * Maturity Date ,emi amount
     */
    private Loans processLoanInformationAndCreateLoan(Loans loans) throws TenureException {
        if (Objects.isNull(loans)) return null;
        Loans loan = loansRepository.save(loans);

        //setting maturity date & loan tenure
        int tenure = loan.getLoanTenureInYears();
        loan.setLoanTenureInYears(tenure);
        LocalDateTime endDate = loan.getStartDate().plusYears(tenure);
        loan.setEndDt(endDate);

        //Calculating emi
        Long loanAmount = loan.getTotalLoan();
        Long emiAmount = calculateEmi(loanAmount, tenure);
        loan.setEmiAmount(emiAmount);

        //initializing the initial value for outstanding amount,amount paid,rate of interest
        Double rate_of_interest = RateOfInterestHelper.getRateOfInterest(tenure);
        loan.setRate_Of_Interest(rate_of_interest);
        loan.setOutstandingAmount(loan.getTotalLoan());
        loan.setAmountPaid(0l);

        //Calculating no of installments & loanTenure
        loan.setTotalInstallmentsInNumber(tenure * MONTHS_IN_YEAR);
        loan.setInstallmentsPaidInNumber(0);
        loan.setInstallmentsRemainingInNumber(tenure * MONTHS_IN_YEAR);

        //saving the processed loan
        Loans finalProcessedLoan = loansRepository.save(loan);
        return finalProcessedLoan;
    }

    /**
     * @param loansDto
     * @paramType LoansDto
     * @returnType LoansDto
     */
    @Override
    public LoansDto borrowLoan(LoansDto loansDto) throws TenureException {
        Loans loan = LoansMapper.mapToLoans(loansDto);
        Loans processedLoan = processLoanInformationAndCreateLoan(loan);
        Loans savedLoan = loansRepository.save(processedLoan);
        return LoansMapper.mapToLoansDto(savedLoan);
    }

    /**
     * @param paymentDto
     * @paramType PaymentDto
     * @returnType PaymentDto
     */
    @Override
    public PaymentDto payInstallments(PaymentDto paymentDto) throws PaymentException, InstallmentsException {
        String methodName = "payInstallments() in LoanServiceImpl";
        Loans currentLoan = loansRepository.findByCustomerIdAndLoanNumber
                (paymentDto.getCustomerId(), paymentDto.getLoanNumber());
        Long emi = currentLoan.getEmiAmount();
        int paidInstallments = currentLoan.getInstallmentsPaidInNumber();
        int remainingInstallments = currentLoan.getInstallmentsRemainingInNumber();
        if (remainingInstallments <= 0)
            throw new InstallmentsException(String.format("Yr loan is already closed"), methodName);


        Long payment = paymentDto.getPaymentAmount();
        int paymentCastedToInt=payment.intValue();
        if (payment < emi || (( paymentCastedToInt % emi) != 0))
            throw new PaymentException(String.format("yr payment %s should be greater equal to or multiple of yr emi %s", payment, emi), methodName);

        //updating the installments data
        int NoOfInstallments = (int) (payment / emi);
        currentLoan.setInstallmentsPaidInNumber(NoOfInstallments + paidInstallments);
        currentLoan.setInstallmentsRemainingInNumber(remainingInstallments - NoOfInstallments);


        //updating the outstanding amount,amount paid
        Long outstandingAmount = currentLoan.getOutstandingAmount();
        Long amountPaid = currentLoan.getAmountPaid();
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
    public List<LoansDto> getAllLoansForCustomerById(Long customerId) {
        List<Loans> allLoans = loansRepository.findAllByCustomerId(customerId);

        List<LoansDto> loansDtoList = allLoans.stream().map(e -> LoansMapper.mapToLoansDto(e)).
                collect(Collectors.toList());
        return loansDtoList;
    }

    /**
     * @param customerId
     * @paramType Long
     * @returnType LoansDto
     */
    @Override
    public LoansDto getInfoAboutLoanByCustomerIdAndLoanNumber(Long customerId, Long loanNumber) {
        Optional<Loans> loan = Optional.ofNullable(loansRepository.findByCustomerIdAndLoanNumber(customerId, loanNumber));
        return LoansMapper.mapToLoansDto(loan.get());
    }
}