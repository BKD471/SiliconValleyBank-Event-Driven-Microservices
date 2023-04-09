package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.exception.AccountsException;

import java.util.List;

public interface IAccountsService {
    AccountsDto createAccounts(AccountsDto accountsDto);
    AccountsDto getAccountInfo(Long accountNumber) throws AccountsException;
    List<AccountsDto> getAllAccountsByCustomerId(Long customerId) throws AccountsException;
    AccountsDto updateAccountDetails(Long accountNumber,AccountsDto accountsDto) throws AccountsException;
    void deleteAccount(Long accountNumber) throws AccountsException;
    void deleteAllAccountsByCustomer(Long customerId) throws  AccountsException;
    void blockAccount(Long accountNumber) throws  AccountsException;
}
