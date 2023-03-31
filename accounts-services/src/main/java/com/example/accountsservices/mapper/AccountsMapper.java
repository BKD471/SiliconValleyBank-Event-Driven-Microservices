package com.example.accountsservices.mapper;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Transactions;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountsMapper {

    /**
     * @param accountsDto
     * @paramType AccountsDto
     * @ReturnType Accounts
     */
    public static Accounts mapToAccounts(AccountsDto accountsDto) {
        Accounts account = new Accounts();
        account.setAccountNumber(accountsDto.getAccountNumber());
        account.setCustomerId(accountsDto.getCustomerId());
        account.setDateOfBirth(accountsDto.getDateOfBirth());
        account.setCustomerAge(accountsDto.getCustomerAge());
        account.setBalance(accountsDto.getBalance());
        account.setAccountType(accountsDto.getAccountType());
        account.setBranchAddress(accountsDto.getBranchAddress());
        account.setPhoneNumber(accountsDto.getPhoneNumber());
        account.setAdharNumber(accountsDto.getAdharNumber());
        account.setPanNumber(accountsDto.getPanNumber());
        account.setVoterId(accountsDto.getVoterId());
        account.setDrivingLicense(accountsDto.getDrivingLicense());
        account.setPassportNumber(accountsDto.getPassportNumber());
        account.setListOfBeneficiary(accountsDto.getListOfBeneficiary());
        account.setListOfTransactions(accountsDto.getListOfTransactions());
        return account;
    }

    /**
     * @param accounts
     * @paramType Accounts
     * @ReturnType AccountsDto
     */
    public static AccountsDto mapToAccountsDto(Accounts accounts) {
        AccountsDto accountsDto = new AccountsDto();
        accountsDto.setAccountNumber(accounts.getAccountNumber());
        accountsDto.setCustomerId(accounts.getCustomerId());
        accountsDto.setDateOfBirth(accounts.getDateOfBirth());
        accountsDto.setCustomerAge(accounts.getCustomerAge());
        accountsDto.setBalance(accounts.getBalance());
        accountsDto.setAccountType(accounts.getAccountType());
        accountsDto.setBranchAddress(accounts.getBranchAddress());
        accountsDto.setPhoneNumber(accounts.getPhoneNumber());
        accountsDto.setAdharNumber(accounts.getAdharNumber());
        accountsDto.setPanNumber(accounts.getPanNumber());
        accountsDto.setVoterId(accounts.getVoterId());
        accountsDto.setDrivingLicense(accounts.getDrivingLicense());
        accountsDto.setPassportNumber(accounts.getPassportNumber());
        accountsDto.setListOfBeneficiary(accounts.getListOfBeneficiary());
        accountsDto.setListOfTransactions(accounts.getListOfTransactions());
        return accountsDto;
    }
}
