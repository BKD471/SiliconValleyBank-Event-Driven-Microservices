package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.dto.OutputDto;
import com.example.accountsservices.exception.AccountsException;

import java.util.List;

public interface IAccountsService {
    OutputDto createAccountForNewUser(InputDto inputDto) throws AccountsException;
    AccountsDto getAccountInfo(Long accountNumber) throws AccountsException;
    List<AccountsDto> getAllActiveAccountsByCustomerId(Long customerId) throws AccountsException;
    OutputDto updateAccountDetails(InputDto inputDto) throws AccountsException;
    AccountsDto getCreditScore(Long accountNUmber);
    AccountsDto updateCreditScore(AccountsDto accountsDto);
    void deleteAccount(Long accountNumber) throws AccountsException;
    void deleteAllAccountsByCustomer(Long customerId) throws  AccountsException;
    void blockAccount(Long accountNumber) throws  AccountsException;
}
