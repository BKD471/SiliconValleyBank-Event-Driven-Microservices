package com.example.accountsservices.mapper;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Transactions;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Mapper {
    /**
     * @param accountsDto
     * @paramType AccountsDto
     * @ReturnType Accounts
     */
    public static Accounts mapToAccounts(AccountsDto accountsDto) {
        Accounts account = new Accounts();
        account.setAccountNumber(accountsDto.getAccountNumber());
        account.setCustomerId(accountsDto.getCustomerId());
        account.setBalance(accountsDto.getBalance());
        account.setAccountType(accountsDto.getAccountType());
        account.setBranchAddress(accountsDto.getBranchAddress());

        account.setDateOfBirth(accountsDto.getDateOfBirth());
        account.setAge(accountsDto.getAge());
        account.setPhoneNumber(accountsDto.getPhoneNumber());
        account.setAdharNumber(accountsDto.getAdharNumber());
        account.setPanNumber(accountsDto.getPanNumber());
        account.setVoterId(accountsDto.getVoterId());
        account.setDrivingLicense(accountsDto.getDrivingLicense());
        account.setPassportNumber(accountsDto.getPassportNumber());
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
        accountsDto.setBalance(accounts.getBalance());
        accountsDto.setAccountType(accounts.getAccountType());
        accountsDto.setAccountStatus(accounts.getAccountStatus());
        accountsDto.setBranchAddress(accounts.getBranchAddress());
        accountsDto.setListOfBeneficiary(accounts.getListOfBeneficiary());
        accountsDto.setListOfTransactions(accounts.getListOfTransactions());

        accountsDto.setDateOfBirth(accounts.getDateOfBirth());
        accountsDto.setAge(accounts.getAge());
        accountsDto.setPhoneNumber(accounts.getPhoneNumber());
        accountsDto.setAdharNumber(accounts.getAdharNumber());
        accountsDto.setPanNumber(accounts.getPanNumber());
        accountsDto.setVoterId(accounts.getVoterId());
        accountsDto.setDrivingLicense(accounts.getDrivingLicense());
        accountsDto.setPassportNumber(accounts.getPassportNumber());
        return accountsDto;
    }

    public static BeneficiaryDto mapToBeneficiaryDto(Beneficiary beneficiary) {
        BeneficiaryDto beneficiaryDto = new BeneficiaryDto();
        beneficiaryDto.setBeneficiaryId(beneficiary.getBeneficiaryId());
        beneficiaryDto.setBeneficiaryName(beneficiary.getBeneficiaryName());
        beneficiaryDto.setAge(beneficiary.getAge());
        beneficiaryDto.setBeneficiaryAccountNumber(beneficiary.getBeneficiaryAccountNumber());
        beneficiaryDto.setAdharNumber(beneficiary.getAdharNumber());
        beneficiaryDto.setRelation(beneficiary.getRelation());
        beneficiaryDto.setVoterId(beneficiary.getVoterId());
        beneficiaryDto.setPanNumber(beneficiary.getPanNumber());
        beneficiaryDto.setPassportNumber(beneficiary.getPassPort());
        beneficiaryDto.setDate_Of_Birth(beneficiary.getDate_Of_Birth());
        beneficiaryDto.setAccounts(beneficiary.getAccounts());
        return beneficiaryDto;
    }

    public static Beneficiary mapToBeneficiary(BeneficiaryDto beneficiaryDto) {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setBeneficiaryName(beneficiaryDto.getBeneficiaryName());
        beneficiary.setBeneficiaryAccountNumber(beneficiaryDto.getBeneficiaryAccountNumber());
        beneficiary.setAdharNumber(beneficiaryDto.getAdharNumber());
        beneficiary.setRelation(beneficiaryDto.getRelation());
        beneficiary.setVoterId(beneficiaryDto.getVoterId());
        beneficiary.setPanNumber(beneficiaryDto.getPanNumber());
        beneficiary.setPassPort(beneficiaryDto.getPassportNumber());
        beneficiary.setDate_Of_Birth(beneficiaryDto.getDate_Of_Birth());
        beneficiary.setAccounts(beneficiaryDto.getAccounts());
        return beneficiary;
    }

    public static Transactions mapToTransactions(TransactionsDto transactionsDto) {
        Transactions transactions = new Transactions();
        transactions.setTransactionAmount(transactionsDto.getTransactionAmount());
        transactions.setTransactedAccountNumber(transactionsDto.getTransactedAccountNumber());
        transactions.setTransactionType(transactionsDto.getTransactionType());
        transactions.setDescription(transactionsDto.getDescription());
        transactions.setAccounts(transactionsDto.getAccounts());
        return transactions;
    }

    public  static  TransactionsDto mapToTransactionsDto(Transactions transactions){
        TransactionsDto transactionsDto=new TransactionsDto();
        transactionsDto.setTransactionAmount(transactions.getTransactionAmount());
        transactionsDto.setTransactionId(transactions.getTransactionId());
        transactionsDto.setTransactedAccountNumber(transactions.getTransactedAccountNumber());
        transactionsDto.setTransactionType(transactions.getTransactionType());
        transactionsDto.setDescription(transactions.getDescription());
        transactionsDto.setAccounts(transactions.getAccounts());
        return transactionsDto;
    }
}