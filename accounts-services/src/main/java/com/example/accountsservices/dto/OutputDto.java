package com.example.accountsservices.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutputDto {
    private String defaultMessage;
    private CustomerOutPutDto customer;
    private AccountsOutPutDto accounts;
    private BeneficiaryDto beneficiary;
    private TransactionsDto transactions;
    private List<AccountsDto> listOfAccounts;
    private List<BeneficiaryDto> beneficiaryList;
    private List<TransactionsDto> transactionsList;

    public OutputDto(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public OutputDto(CustomerOutPutDto customerDto, List<AccountsDto> listOfAccounts,
                     String message) {
        this.listOfAccounts = listOfAccounts;
        this.customer = customerDto;
        this.defaultMessage = message;
    }

    public OutputDto(CustomerOutPutDto customerDto, AccountsOutPutDto accounts,
                     String message) {
        this.customer = customerDto;
        this.accounts = accounts;
        this.defaultMessage = message;
    }

    public OutputDto(CustomerOutPutDto customer, AccountsOutPutDto accounts,
                     List<BeneficiaryDto> beneficiaryList, String message) {
        this.customer = customer;
        this.accounts = accounts;
        this.beneficiaryList = beneficiaryList;
        this.defaultMessage = message;
    }

    public OutputDto(CustomerOutPutDto customer,
                     AccountsOutPutDto accounts,
                     BeneficiaryDto beneficiaryDto, String message) {
        this.customer = customer;
        this.accounts = accounts;
        this.beneficiary = beneficiaryDto;
        this.defaultMessage = message;
    }

    public OutputDto(CustomerOutPutDto customer, AccountsOutPutDto accounts,
                     TransactionsDto transactions, String defaultMessage) {
        this.customer = customer;
        this.accounts = accounts;
        this.transactions = transactions;
        this.defaultMessage = defaultMessage;
    }

     //Note because of type erasure, List<BeneficiaryDto> & List<TransactionsDto> will clash
    //so use ArrayList instead
    public OutputDto(CustomerOutPutDto customer, AccountsOutPutDto accounts,
                     ArrayList<TransactionsDto> transactionsList, String defaultMessage){
        this.customer=customer;
        this.accounts=accounts;
        this.transactionsList=transactionsList;
        this.defaultMessage=defaultMessage;
    }

}
