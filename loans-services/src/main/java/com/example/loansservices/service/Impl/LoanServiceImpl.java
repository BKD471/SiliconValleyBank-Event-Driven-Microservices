package com.example.loansservices.service.Impl;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.excpetion.TenureException;
import com.example.loansservices.mapper.LoansMapper;
import com.example.loansservices.model.Loans;
import com.example.loansservices.repository.LoansRepository;
import com.example.loansservices.service.LoansService;
import com.example.loansservices.utils.RateOfInterestHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;


@Service
public class LoanServiceImpl implements LoansService {
    private static final int MONTHS_IN_YEAR=12;
    private static final int PERCENTAGE=100;

    private LoansRepository loansRepository;

    LoanServiceImpl(LoansRepository loansRepository){
        this.loansRepository=loansRepository;
    }

    /**
     * Method to calculate emi
     * */
    private Double calculateEmi(Double loanAmount,int tenure) throws TenureException {

         Double rate_of_interest= RateOfInterestHelper.getRateOfInterest(tenure);
         Double magic_coeff=((rate_of_interest/PERCENTAGE)/MONTHS_IN_YEAR);
         Double interest=loanAmount*magic_coeff;

         Double numerator=Math.pow(1+magic_coeff,tenure*MONTHS_IN_YEAR);
         Double denominator=numerator-1;

         Double emi_coeff=(numerator/denominator);
         return interest*emi_coeff;
    }

    /**
     *  Method to process critical information like  no of installments,
     *  Maturity Date ,emi amount
     * */
    private Loans processLoanInformationAndCreateLoan(Loans loans) throws TenureException {
        if(Objects.isNull(loans)) return null;
        Loans loan=loansRepository.save(loans);

        //setting maturity date
        int tenure=loan.getLoanTenureInYears();
        LocalDateTime endDate=loan.getStartDate().plusYears(tenure);
        loan.setEndDt(endDate);

        //Calculating emi
        Double loanAmount=loan.getTotalLoan();
        Double emiAmnt=calculateEmi(loanAmount,tenure);
        loan.setEmiAmount(emiAmnt);

        //Calculating no of installments
        loan.setTotalInstallmentsInNumber(tenure*12);
        loan.setInstallmentsPaidInNumber(0);
        loan.setInstallmentsRemainingInNumber(tenure*12);


        Loans finalProcessedLoan=loansRepository.save(loan);
        return  finalProcessedLoan;
    }

    /**
     * @param loansDto
     * @ParamType LoansDto
     * @returnType LoansDto
     */
    @Override
    public LoansDto borrowLoan(LoansDto loansDto) throws TenureException {
        Loans loan= LoansMapper.mapToLoans(loansDto);
        Loans processedLoan=processLoanInformationAndCreateLoan(loan);
        Loans savedLoan=loansRepository.save(processedLoan);
        return LoansMapper.mapToLoansDto(savedLoan);
    }

    /**
     * @param loansDto
     * @ParamType LoansDto
     * @returnType LoansDto
     */
    @Override
    public LoansDto payInstallMents(LoansDto loansDto) throws TenureException {
        Loans loan= LoansMapper.mapToLoans(loansDto);

        return null;
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
