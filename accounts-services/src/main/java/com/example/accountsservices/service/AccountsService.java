package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.model.Accounts;

public interface AccountsService {
    AccountsDto createAccounts(AccountsDto accountsDto);
    AccountsDto getAccountByCustomerId(Long id);
    AccountsDto updateAccountByCustomerIdAndAccountNumber(Long customerId,Long accountNumber);
    AccountsDto updateBeneficiaryDetails(Long customerId);
    AccountsDto credit(Long customerId,Long accountNumber);

}
