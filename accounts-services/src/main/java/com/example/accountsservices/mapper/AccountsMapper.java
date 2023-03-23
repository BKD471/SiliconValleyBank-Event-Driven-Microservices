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
        Accounts acnt = new Accounts();
        acnt.setAccountType(accountsDto.getAccountType());
        acnt.setBranchAddress(accountsDto.getBranchAddress());
        acnt.setCustomerId(accountsDto.getCustomerId());
        return acnt;
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
