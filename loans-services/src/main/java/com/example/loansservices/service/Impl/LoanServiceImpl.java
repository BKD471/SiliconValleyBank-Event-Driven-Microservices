package com.example.loansservices.service.Impl;

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
import java.util.Objects;


@Service
@Slf4j
public class LoanServiceImpl implements LoansService {
    private static final int MONTHS_IN_YEAR = 12;
    private static final int PERCENTAGE = 100;

    private LoansRepository loansRepository;

    LoanServiceImpl(LoansRepository loansRepository) {
        this.loansRepository = loansRepository;
    }

    /**
     * Method to calculate emi
     */
    private Double calculateEmi(Double loanAmount, int tenure) {
        Double interest = 0.0d;
        Double emi_coeff = 0.0d;
        String methodName = "calculateEmi() in LoanServiceImpl";
        try {
            log.info("Emi Calculation ha started ----------------------------------->", LocalDateTime.now());
            Double rate_of_interest = RateOfInterestHelper.getRateOfInterest(tenure);
            Double magic_coeff = ((rate_of_interest / PERCENTAGE) / MONTHS_IN_YEAR);
            interest = loanAmount * magic_coeff;

            Double numerator = Math.pow(1 + magic_coeff, tenure * MONTHS_IN_YEAR);
            Double denominator = numerator - 1;

            emi_coeff = (numerator / denominator);
            log.info("Emi Calculation have finished ----------------------------------->", LocalDateTime.now());
        } catch (TenureException e) {
            log.info(String.format("%s exception has occurred in %s method", e, methodName));
            e.getMessage();
        }
        return interest * emi_coeff;
    }

    /**
     * Method to process critical information like  no of installments,
     * Maturity Date ,emi amount
     */
    private Loans processLoanInformationAndCreateLoan(Loans loans) {
        if (Objects.isNull(loans)) return null;
        log.info("Loan is being processed--------------------------------------------------->", LocalDateTime.now());
        Loans loan = loansRepository.save(loans);

        //setting maturity date
        int tenure = loan.getLoanTenureInYears();
        LocalDateTime endDate = loan.getStartDate().plusYears(tenure);
        loan.setEndDt(endDate);

        //Calculating emi
        Double loanAmount = loan.getTotalLoan();
        Double emiAmnt = calculateEmi(loanAmount, tenure);
        loan.setEmiAmount(emiAmnt);

        //Calculating no of installments
        loan.setTotalInstallmentsInNumber(tenure * MONTHS_IN_YEAR);
        loan.setInstallmentsPaidInNumber(0);
        loan.setInstallmentsRemainingInNumber(tenure * MONTHS_IN_YEAR);

        //saving the processed loan
        Loans finalProcessedLoan = loansRepository.save(loan);
        log.info("Loan has been processed------------------------------------------------------>", LocalDateTime.now());
        return finalProcessedLoan;
    }

    /**
     * @param loansDto
     * @ParamType LoansDto
     * @returnType LoansDto
     */
    @Override
    public LoansDto borrowLoan(LoansDto loansDto) {
        log.info("Request for loan has been received--------------------------------------->", LocalDateTime.now());
        Loans loan = LoansMapper.mapToLoans(loansDto);
        Loans processedLoan = processLoanInformationAndCreateLoan(loan);
        Loans savedLoan = loansRepository.save(processedLoan);
        log.info("Request for Loan has been granted---------------------------------------->", LocalDateTime.now());
        return LoansMapper.mapToLoansDto(savedLoan);
    }

    /**
     * @param paymentDto
     * @ParamType PaymentDto
     * @returnType PaymentDto
     */
    @Override
    public PaymentDto payInstallments(PaymentDto paymentDto) throws PaymentException, InstallmentsException {
        String methodName = "payInstallments() in LoanServiceImpl";
        log.info("Emi Payment is being initiated.............................................>");
        Loans currentLoan = loansRepository.findByCustomerIdAndLoanNumber
                (paymentDto.getCustomerId(), paymentDto.getLoanNumber());
        Double emi = currentLoan.getEmiAmount();
        int paidInstallments = currentLoan.getInstallmentsPaidInNumber();
        int remainingInstallments = currentLoan.getInstallmentsRemainingInNumber();
        if (remainingInstallments <= 0)
            throw new InstallmentsException(String.format("Yr loan is already closed"), methodName, LocalDateTime.now());


        Double payment = paymentDto.getPaymentAmount();
        if (payment < emi || payment % emi != 0)
            throw new PaymentException(String.format("yr payment %s should be greater equal to or multiple of yr emi %s", payment, emi), methodName, LocalDateTime.now());


        int NoOfInstallments = (int) (payment / emi);
        currentLoan.setInstallmentsPaidInNumber(NoOfInstallments + paidInstallments);
        currentLoan.setInstallmentsRemainingInNumber(remainingInstallments - NoOfInstallments);

        Loans paidEmi = loansRepository.save(currentLoan);
        log.info("Payment for Emi is successfully received................................>");
        return LoansMapper.mapToPaymentDto(paidEmi, payment);
    }

    /**
     * @param customerId
     * @ParamType Long
     * @returnType LoansDto
     */
    @Override
    public LoansDto getInfoAboutLoans(Long customerId) {
        return null;
    }
}
