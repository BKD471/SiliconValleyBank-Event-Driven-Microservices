package com.example.accountsservices.dto;

import lombok.*;

import java.util.List;

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
    private List<AccountsDto> listOfAccounts;
    private List<BeneficiaryDto> beneficiaryList;

    public OutputDto(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public OutputDto(CustomerOutPutDto customerDto, List<AccountsDto> listOfAccounts, String message) {
        this.listOfAccounts = listOfAccounts;
        this.customer = customerDto;
        this.defaultMessage = message;
    }

    public OutputDto(CustomerOutPutDto customerDto, AccountsOutPutDto accounts, String message) {
        this.customer = customerDto;
        this.accounts = accounts;
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
                     List<BeneficiaryDto> beneficiaryDto, String message) {
        this.customer = customer;
        this.accounts = accounts;
        this.beneficiaryList = beneficiaryDto;
        this.defaultMessage = message;
    }
}
