package com.example.accountsservices.mapper;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.model.Accounts;

public class AccountsMapper {

    /**
     * @param accountsDto
     * @paramType AccountsDto
     * @ReturnType Accounts
     */
    public static Accounts mapToAccounts(AccountsDto accountsDto) {
        Accounts acount = new Accounts();
        acount.setAccountType(accountsDto.getAccountType());
        acount.setBranchAddress(accountsDto.getBranchAddress());
        acount.setCustomerId(accountsDto.getCustomerId());
        return acount;
    }

    /**
     * @param accounts
     * @paramType Accounts
     * @ReturnType AccountsDto
     */
    public static AccountsDto mapToAccountsDto(Accounts accounts) {
        AccountsDto accountsDto = new AccountsDto();
        accountsDto.setAccountNumber(accounts.getAccountNumber());
        accountsDto.setAccountType(accounts.getAccountType());
        accountsDto.setCustomerId(accounts.getCustomerId());
        accountsDto.setBranchAddress(accounts.getBranchAddress());
        return accountsDto;
    }
}
