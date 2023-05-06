package com.example.accountsservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutputDto {
    private String defaultMessage;
    private CustomerOutPutDto customer;
    private AccountsOutPutDto accounts;

    private List<AccountsDto> listOfAccounts;
    public OutputDto(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }


    public  OutputDto(CustomerOutPutDto customerDto,List<AccountsDto> listOfAccounts,String message){
        this.listOfAccounts=listOfAccounts;
        this.customer=customerDto;
        this.defaultMessage=message;
    }


}
