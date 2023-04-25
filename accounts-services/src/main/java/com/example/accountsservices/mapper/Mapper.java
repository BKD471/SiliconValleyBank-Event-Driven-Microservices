package com.example.accountsservices.mapper;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
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
        account.setCustomer(accountsDto.getCustomer());
        account.setBalance(accountsDto.getBalance());
        account.setAccountType(accountsDto.getAccountType());
        account.setHomeBranch(accountsDto.getHomeBranch());
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
        accountsDto.setBalance(accounts.getBalance());
        accountsDto.setAccountType(accounts.getAccountType());
        accountsDto.setAccountStatus(accounts.getAccountStatus());
        accountsDto.setBranchCode(accounts.getBranchCode());
        accountsDto.setHomeBranch(accounts.getHomeBranch());
        accountsDto.setTransferLimitPerDay(accounts.getTransferLimitPerDay());
        accountsDto.setCreditScore(accounts.getCreditScore());
        accountsDto.setListOfBeneficiary(accounts.getListOfBeneficiary());
        accountsDto.setListOfTransactions(accounts.getListOfTransactions());
        accountsDto.setCustomer(accounts.getCustomer());
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
        beneficiaryDto.setPassportNumber(beneficiary.getPassportNumber());
        beneficiaryDto.setDateOfBirth(beneficiary.getDate_Of_Birth());
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
        beneficiary.setPassportNumber(beneficiaryDto.getPassportNumber());
        beneficiary.setDate_Of_Birth(beneficiaryDto.getDateOfBirth());
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
        transactionsDto.setTransactionTimeStamp(transactions.getTransactionTimeStamp());
        transactionsDto.setTransactionId(transactions.getTransactionId());
        transactionsDto.setTransactedAccountNumber(transactions.getTransactedAccountNumber());
        transactionsDto.setTransactionType(transactions.getTransactionType());
        transactionsDto.setDescription(transactions.getDescription());
        transactionsDto.setAccounts(transactions.getAccounts());
        return transactionsDto;
    }


    public  static Accounts inputToAccounts(InputDto inputDto){
        Accounts accounts=new Accounts();
        accounts.setAccountType(inputDto.getAccountType());
        accounts.setHomeBranch(inputDto.getHomeBranch());
        return accounts;
    }

//    public  static AccountsDto inputToAccountsDto(InputDto inputDto){
//        AccountsDto accountsDto=new AccountsDto();
//
//        inputDto.setAccountNumber(accounts.getAccountNumber());
//        inputDto.setBalance(accounts.getBalance());
//        inputDto.setAccountType(accounts.getAccountType());
//        inputDto.setBranchCode(accounts.getBranchCode());
//        inputDto.setHomeBranch(accounts.getHomeBranch());
//        inputDto.setTransferLimitPerDay(accounts.getTransferLimitPerDay());
//        inputDto.setCreditScore(accounts.getCreditScore());
//        inputDto.setAccountStatus(accounts.getAccountStatus());
//        inputDto.setListOfTransactions(accounts.getListOfTransactions());
//        inputDto.setListOfBeneficiary(accounts.getListOfBeneficiary());
//        return accountsDto;
//    }

    public  static Customer inputToCustomer(InputDto inputDto){
        Customer customer=new Customer();
        customer.setName(inputDto.getName());

        //converting date to its desired type
        String date[]=inputDto.getDateOfBirthInYMD().split("-");
        LocalDate dob=LocalDate.of(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]));
        customer.setDateOfBirth(dob);

        customer.setEmail(inputDto.getEmail());
        customer.setPhoneNumber(inputDto.getPhoneNumber());
        customer.setAdharNumber(inputDto.getAdharNumber());
        customer.setPanNumber(inputDto.getPanNumber());
        customer.setVoterId(inputDto.getVoterId());
        customer.setDrivingLicense(inputDto.getDrivingLicense());
        customer.setPassportNumber(inputDto.getPassportNumber());
        return customer;
    }

    public  static InputDto mapToinputDto(Customer customer,Accounts accounts){
        InputDto inputDto=new InputDto();

        //set customer info
        inputDto.setName(customer.getName());
        inputDto.setDateOfBirthInYMD(customer.getDateOfBirth().toString());
        inputDto.setAge(customer.getAge());
        inputDto.setEmail(customer.getEmail());
        inputDto.setPhoneNumber(customer.getPhoneNumber());
        inputDto.setAdharNumber(customer.getAdharNumber());
        inputDto.setPanNumber(customer.getPanNumber());
        inputDto.setVoterId(customer.getVoterId());
        inputDto.setDrivingLicense(customer.getDrivingLicense());
        inputDto.setPassportNumber(customer.getPassportNumber());

        //set account
        inputDto.setAccountNumber(accounts.getAccountNumber());
        inputDto.setBalance(accounts.getBalance());
        inputDto.setAccountType(accounts.getAccountType());
        inputDto.setBranchCode(accounts.getBranchCode());
        inputDto.setHomeBranch(accounts.getHomeBranch());
        inputDto.setTransferLimitPerDay(accounts.getTransferLimitPerDay());
        inputDto.setCreditScore(accounts.getCreditScore());
        inputDto.setAccountStatus(accounts.getAccountStatus());
        inputDto.setListOfTransactions(accounts.getListOfTransactions());
        inputDto.setListOfBeneficiary(accounts.getListOfBeneficiary());
        return inputDto;
    }

}
