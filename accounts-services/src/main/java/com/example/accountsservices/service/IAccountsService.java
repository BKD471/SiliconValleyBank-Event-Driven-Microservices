package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.dto.OutputDto;
import com.example.accountsservices.exception.AccountsException;

import java.util.List;

public interface IAccountsService {
    AccountsDto getAccountInfo(Long accountNumber) throws AccountsException;
    List<AccountsDto> getAllActiveAccountsByCustomerId(Long customerId) throws AccountsException;
    OutputDto requestExecutor(InputDto inputDto) throws AccountsException;

}
