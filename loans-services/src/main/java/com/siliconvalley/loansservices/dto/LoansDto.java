package com.siliconvalley.loansservices.dto;

import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import com.siliconvalley.loansservices.model.Loans;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;



public record LoansDto(String loanNumber,String customerId,LocalDate endDate,LocalDate startDate,
         AllConstantsHelper.LoanType loanType,BigDecimal totalLoan,int loanTenureInYears,
         BigDecimal paymentAmount,BigDecimal amountPaid,BigDecimal emiAmount,Double Rate_of_Interest,
         int totalInstallmentsInNumber,int installmentsPaidInNumber,int installmentsRemainingInNumber,
         BigDecimal outstandingAmount,AllConstantsHelper.RequestType requestType,AllConstantsHelper.FormatType formatType,
         Set<Loans> loansSet){

    public LoansDto withLoanNumber(String loanNumber) {
        return new LoansDto(loanNumber,customerId(),endDate(),startDate(),loanType(),totalLoan(),loanTenureInYears(),
                paymentAmount(),amountPaid(),emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType(),loansSet());
    }

    public LoansDto withCustomerId(String customerId) {
        return new LoansDto(loanNumber(),customerId,endDate(),startDate(),loanType(),totalLoan(),loanTenureInYears(),
                paymentAmount(),amountPaid(),emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType(),loansSet());
    }

    public LoansDto withEndDate(LocalDate endDate) {
        return new LoansDto(loanNumber(),customerId(),endDate,startDate(),loanType(),totalLoan(),loanTenureInYears(),
                paymentAmount(),amountPaid(),emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType(),loansSet());
    }

    public LoansDto withStartDate(LocalDate startDate) {
        return new LoansDto(loanNumber(),customerId(),endDate(),startDate,loanType(),totalLoan(),loanTenureInYears(),
                paymentAmount(),amountPaid(),emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType(),loansSet());
    }

    public LoansDto withLoanType(AllConstantsHelper.LoanType loanType) {
        return new LoansDto(loanNumber(),customerId(),endDate(),startDate(),loanType,totalLoan(),loanTenureInYears(),
                paymentAmount(),amountPaid(),emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType(),loansSet());
    }

    public LoansDto withTotalLoan(BigDecimal totalLoan) {
        return new LoansDto(loanNumber(),customerId(),endDate(),startDate(),loanType(),totalLoan,loanTenureInYears(),
                paymentAmount(),amountPaid(),emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType(),loansSet());
    }

    public LoansDto withLoanTenureInYears(Integer loanTenureInYears) {
        return new LoansDto(loanNumber(),customerId(),endDate(),startDate(),loanType(),totalLoan(),loanTenureInYears,
                paymentAmount(),amountPaid(),emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType(),loansSet());
    }

    public LoansDto withPaymentAmount(BigDecimal paymentAmount) {
        return new LoansDto(loanNumber(),customerId(),endDate(),startDate(),loanType(),totalLoan(),loanTenureInYears(),
                paymentAmount,amountPaid(),emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType(),loansSet());
    }

    public LoansDto withAmountPaid(BigDecimal amountPaid) {
        return new LoansDto(loanNumber(),customerId(),endDate(),startDate(),loanType(),totalLoan(),loanTenureInYears(),
                paymentAmount(),amountPaid,emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType(),loansSet());
    }

    public LoansDto withEmiAmount(BigDecimal emiAmount) {
        return new LoansDto(loanNumber(),customerId(),endDate(),startDate(),loanType(),totalLoan(),loanTenureInYears(),
                paymentAmount(),amountPaid(),emiAmount,Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType(),loansSet());
    }

    public LoansDto withRate_of_Interest(Double Rate_of_Interest) {
        return new LoansDto(loanNumber(),customerId(),endDate(),startDate(),loanType(),totalLoan(),loanTenureInYears(),
                paymentAmount(),amountPaid(),emiAmount(),Rate_of_Interest,totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType(),loansSet());
    }

    public LoansDto withTotalInstallmentsInNumber(int totalInstallmentsInNumber) {
        return new LoansDto(loanNumber(),customerId(),endDate(),startDate(),loanType(),totalLoan(),loanTenureInYears(),
                paymentAmount(),amountPaid(),emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber,
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType(),loansSet());
    }

    public LoansDto withInstallmentsPaidInNumber(int installmentsPaidInNumber) {
        return new LoansDto(loanNumber(),customerId(),endDate(),startDate(),loanType(),totalLoan(),loanTenureInYears(),
                paymentAmount(),amountPaid(),emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber,installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType(),loansSet());
    }

    public LoansDto withOutstandingAmount(BigDecimal outstandingAmount) {
        return new LoansDto(loanNumber(),customerId(),endDate(),startDate(),loanType(),totalLoan(),loanTenureInYears(),
                paymentAmount(),amountPaid(),emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount,requestType(),
                formatType(),loansSet());
    }

    public LoansDto withRequestType(AllConstantsHelper.RequestType requestType) {
        return new LoansDto(loanNumber(),customerId(),endDate(),startDate(),loanType(),totalLoan(),loanTenureInYears(),
                paymentAmount(),amountPaid(),emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType,
                formatType(),loansSet());
    }

    public LoansDto withFormatType(AllConstantsHelper.FormatType formatType) {
        return new LoansDto(loanNumber(),customerId(),endDate(),startDate(),loanType(),totalLoan(),loanTenureInYears(),
                paymentAmount(),amountPaid(),emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType,loansSet());
    }

    public LoansDto withLoansSet(Set<Loans> loansSet) {
        return new LoansDto(loanNumber(),customerId(),endDate(),startDate(),loanType(),totalLoan(),loanTenureInYears(),
                paymentAmount(),amountPaid(),emiAmount(),Rate_of_Interest(),totalInstallmentsInNumber(),
                installmentsPaidInNumber(),installmentsRemainingInNumber(),outstandingAmount(),requestType(),
                formatType(),loansSet);
    }

    public static  final class Builder{
        private String loanNumber;
        private String customerId;
        private LocalDate endDate;
        private LocalDate startDate;
        private AllConstantsHelper.LoanType loanType;
        private BigDecimal totalLoan;
        private int loanTenureInYears;
        private BigDecimal paymentAmount;
        private BigDecimal amountPaid;
        private BigDecimal emiAmount;
        private Double Rate_of_Interest;
        private int totalInstallmentsInNumber;
        private int installmentsPaidInNumber;
        private int installmentsRemainingInNumber;
        private BigDecimal outstandingAmount;
        private AllConstantsHelper.RequestType requestType;
        private AllConstantsHelper.FormatType formatType;
        private Set<Loans> loansSet;

        public Builder(){}

        public Builder loanNumber(String loanNumber){
            this.loanNumber=loanNumber;
            return this;
        }

        public Builder customerId(String customerId){
            this.customerId=customerId;
            return this;
        }

        public Builder endDate(LocalDate endDate){
            this.endDate=endDate;
            return this;
        }

        public Builder startDate(LocalDate startDate){
            this.startDate=startDate;
            return this;
        }

        public Builder loanType(AllConstantsHelper.LoanType loanType){
            this.loanType=loanType;
            return this;
        }

        public Builder totalLoan(BigDecimal totalLoan){
            this.totalLoan=totalLoan;
            return this;
        }

        public Builder loanTenureInYears(int loanTenureInYears){
            this.loanTenureInYears=loanTenureInYears;
            return this;
        }

        public Builder paymentAmount(BigDecimal paymentAmount){
            this.paymentAmount=paymentAmount;
            return this;
        }

        public Builder amountPaid(BigDecimal amountPaid){
            this.amountPaid=amountPaid;
            return this;
        }

        public Builder emiAmount(BigDecimal emiAmount){
            this.emiAmount=emiAmount;
            return this;
        }

        public Builder Rate_of_Interest(Double Rate_of_Interest){
            this.Rate_of_Interest=Rate_of_Interest;
            return this;
        }

        public Builder totalInstallmentsInNumber(int totalInstallmentsInNumber){
            this.totalInstallmentsInNumber=totalInstallmentsInNumber;
            return this;
        }

        public Builder installmentsPaidInNumber(int installmentsPaidInNumber){
            this.installmentsPaidInNumber=installmentsPaidInNumber;
            return this;
        }

        public Builder installmentsRemainingInNumber(int installmentsRemainingInNumber){
            this.installmentsRemainingInNumber=installmentsRemainingInNumber;
            return this;
        }

        public Builder outstandingAmount(BigDecimal outstandingAmount){
            this.outstandingAmount=outstandingAmount;
            return this;
        }

        public Builder requestType(AllConstantsHelper.RequestType requestType){
            this.requestType=requestType;
            return this;
        }

        public Builder formatType(AllConstantsHelper.FormatType formatType){
            this.formatType=formatType;
            return this;
        }

        public Builder loansSet(Set<Loans> loansSet){
            this.loansSet=loansSet;
            return this;
        }

       public LoansDto build(){
            return new LoansDto(loanNumber,customerId,endDate,startDate,loanType,totalLoan,loanTenureInYears,
                    paymentAmount,amountPaid,emiAmount,Rate_of_Interest,totalInstallmentsInNumber,
                    installmentsPaidInNumber,installmentsRemainingInNumber,outstandingAmount,requestType,
                    formatType,loansSet);
       }
    }
}


