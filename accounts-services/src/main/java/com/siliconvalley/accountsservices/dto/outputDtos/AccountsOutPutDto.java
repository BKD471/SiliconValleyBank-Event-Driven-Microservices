package com.siliconvalley.accountsservices.dto.outputDtos;

import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;

import java.math.BigDecimal;
import java.util.Set;

public record AccountsOutPutDto(String accountNumber,BigDecimal balance,AllConstantHelpers.Branch homeBranch,
                                AllConstantHelpers.AccountType accountType,
                                AllConstantHelpers.AccountStatus accountStatus,
                                String branchCode,BigDecimal transferLimitPerDay,
                                int creditScore,BigDecimal approvedLoanLimitBasedOnCreditScore,
                                Boolean anyActiveLoans,BigDecimal totalOutstandingAmountPayableToBank,
                                BigDecimal totLoanIssuedSoFar,Set<BeneficiaryDto> listOfBeneficiary,
                                Set<TransactionsDto> listOfTransactions){

    public static final class Builder{
        private String accountNumber;
        private BigDecimal balance;
        private AllConstantHelpers.Branch homeBranch;
        private AllConstantHelpers.AccountType accountType;
        private AllConstantHelpers.AccountStatus accountStatus;
        private String branchCode;
        private BigDecimal transferLimitPerDay;
        private int creditScore;
        private BigDecimal approvedLoanLimitBasedOnCreditScore;
        private Boolean anyActiveLoans;
        private BigDecimal totalOutstandingAmountPayableToBank;
        private BigDecimal totLoanIssuedSoFar;
        private Set<BeneficiaryDto> listOfBeneficiary;
        private Set<TransactionsDto> listOfTransactions;

        public Builder(){}

        public Builder balance(BigDecimal balance){
            this.balance=balance;
            return this;
        }

        public Builder accountNumber(String accountNumber){
            this.accountNumber=accountNumber;
            return this;
        }

        public Builder homeBranch(AllConstantHelpers.Branch homeBranch){
            this.homeBranch=homeBranch;
            return this;
        }

        public Builder accountType(AllConstantHelpers.AccountType accountType){
            this.accountType=accountType;
            return this;
        }

        public Builder accountStatus(AllConstantHelpers.AccountStatus accountStatus){
            this.accountStatus=accountStatus;
            return this;
        }

        public Builder branchCode(String branchCode){
            this.branchCode=branchCode;
            return this;
        }

        public Builder transferLimitPerDay(BigDecimal transferLimitPerDay){
            this.transferLimitPerDay=transferLimitPerDay;
            return this;
        }

        public Builder creditScore(int creditScore){
            this.creditScore=creditScore;
            return this;
        }

        public Builder approvedLoanLimitBasedOnCreditScore(BigDecimal approvedLoanLimitBasedOnCreditScore){
            this.approvedLoanLimitBasedOnCreditScore=approvedLoanLimitBasedOnCreditScore;
            return this;
        }

        public Builder anyActiveLoans(Boolean anyActiveLoans){
            this.anyActiveLoans=anyActiveLoans;
            return this;
        }

        public Builder totalOutstandingAmountPayableToBank(BigDecimal totalOutstandingAmountPayableToBank){
            this.totalOutstandingAmountPayableToBank=totalOutstandingAmountPayableToBank;
            return this;
        }

        public Builder totLoanIssuedSoFar(BigDecimal totLoanIssuedSoFar){
            this.totLoanIssuedSoFar=totLoanIssuedSoFar;
            return this;
        }

        public Builder listOfBeneficiary(Set<BeneficiaryDto> listOfBeneficiary){
            this.listOfBeneficiary=listOfBeneficiary;
            return this;
        }

        public Builder listOfTransactions(Set<TransactionsDto> listOfTransactions){
            this.listOfTransactions=listOfTransactions;
            return this;
        }

        public AccountsOutPutDto build(){
            return new AccountsOutPutDto(accountNumber,balance,homeBranch,accountType,accountStatus,branchCode,
                    transferLimitPerDay,creditScore,
                    approvedLoanLimitBasedOnCreditScore,anyActiveLoans,totalOutstandingAmountPayableToBank,totLoanIssuedSoFar,listOfBeneficiary,listOfTransactions);
        }
    }
}