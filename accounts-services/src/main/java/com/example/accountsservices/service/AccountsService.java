package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.model.Accounts;

public interface AccountsService {
    Accounts createAccounts(AccountsDto accountsDto);
    Accounts getAccountByCustomerId(Long id);
}
