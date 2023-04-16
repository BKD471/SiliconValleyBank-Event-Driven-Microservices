package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.exception.AccountsException;

import java.util.List;

public interface IAccountsService {
    AccountsDto createAccounts(AccountsDto accountsDto);
    AccountsDto getAccountInfo(Long accountNumber) throws AccountsException;
    List<AccountsDto> getAllActiveAccountsByCustomerId(Long customerId) throws AccountsException;
    AccountsDto updateAccountDetails(AccountsDto accountsDto) throws AccountsException;
    AccountsDto getCreditScore(Long accountNUmber);
    AccountsDto updateCreditScore(AccountsDto accountsDto);
    void deleteAccount(Long accountNumber) throws AccountsException;
    void deleteAllAccountsByCustomer(Long customerId) throws  AccountsException;
    void blockAccount(Long accountNumber) throws  AccountsException;
}
