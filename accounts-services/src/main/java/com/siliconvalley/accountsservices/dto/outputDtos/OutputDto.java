package com.siliconvalley.accountsservices.dto.outputDtos;

import com.siliconvalley.accountsservices.dto.baseDtos.AccountsDto;
import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.dto.responseDtos.PageableResponseDto;
import java.util.Set;


public record  OutputDto(String defaultMessage,CustomerOutPutDto customer,
                         AccountsOutPutDto accounts,BeneficiaryDto beneficiary,
                         TransactionsDto transactions,PageableResponseDto<AccountsDto> accountsListPages,
                         PageableResponseDto<CustomerDto> customerListPages,PageableResponseDto<BeneficiaryDto> beneficiaryListPages,
    Set<AccountsDto> listOfAccounts,Set<BeneficiaryDto> beneficiaryList,Set<TransactionsDto> transactionsList){
    public static final class Builder{
        private String defaultMessage;
        private CustomerOutPutDto customer;
        private AccountsOutPutDto accounts;
        private BeneficiaryDto beneficiary;
        private TransactionsDto transactions;
        private PageableResponseDto<AccountsDto> accountsListPages;
        private PageableResponseDto<CustomerDto> customerListPages;
        private PageableResponseDto<BeneficiaryDto> beneficiaryListPages;
        private Set<AccountsDto> listOfAccounts;
        private Set<BeneficiaryDto> beneficiaryList;
        private Set<TransactionsDto> transactionsList;

        public Builder(){}

        public Builder defaultMessage(String defaultMessage){
            this.defaultMessage=defaultMessage;
            return this;
        }

        public Builder customer(CustomerOutPutDto customer){
            this.customer=customer;
            return this;
        }

        public Builder accounts(AccountsOutPutDto accounts){
            this.accounts=accounts;
            return this;
        }

        public Builder beneficiary(BeneficiaryDto beneficiary){
            this.beneficiary=beneficiary;
            return this;
        }

        public Builder transactions(TransactionsDto transactions){
            this.transactions=transactions;
            return this;
        }

        public Builder accountsListPages(PageableResponseDto<AccountsDto> accountsListPages){
            this.accountsListPages=accountsListPages;
            return this;
        }

        public Builder customerListPages(PageableResponseDto<CustomerDto> customerListPages){
            this.customerListPages=customerListPages;
            return this;
        }

        public Builder beneficiaryListPages(PageableResponseDto<BeneficiaryDto> beneficiaryListPages){
            this.beneficiaryListPages=beneficiaryListPages;
            return this;
        }

        public Builder listOfAccounts(Set<AccountsDto> listOfAccounts){
            this.listOfAccounts=listOfAccounts;
            return this;
        }

        public Builder beneficiaryList(Set<BeneficiaryDto> beneficiaryList){
            this.beneficiaryList=beneficiaryList;
            return this;
        }

        public Builder transactionsList(Set<TransactionsDto> transactionsList){
            this.transactionsList=transactionsList;
            return this;
        }

        public OutputDto build(){
            return new OutputDto(defaultMessage,customer,accounts,beneficiary,transactions,
                    accountsListPages,customerListPages,beneficiaryListPages,listOfAccounts,beneficiaryList,transactionsList);
        }
    }
}

