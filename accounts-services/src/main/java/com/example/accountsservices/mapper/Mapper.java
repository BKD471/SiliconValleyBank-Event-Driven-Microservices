package com.example.accountsservices.mapper;

import com.example.accountsservices.dto.*;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.model.Transactions;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    /**
     * @param accountsDto
     * @paramType AccountsDto
     * @ReturnType Accounts
     */
    public static Accounts mapToAccounts(AccountsDto accountsDto) {
        Accounts account = new Accounts();
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
        accountsDto.setBranchCode(accounts.getBranchCode());
        accountsDto.setHomeBranch(accounts.getHomeBranch());
        accountsDto.setTransferLimitPerDay(accounts.getTransferLimitPerDay());
        accountsDto.setCreditScore(accounts.getCreditScore());
        accountsDto.setApprovedLoanLimitBasedOnCreditScore(accounts.getApprovedLoanLimitBasedOnCreditScore());
        accountsDto.setAnyActiveLoans(accounts.getAnyActiveLoans());
        accountsDto.setTotLoanIssuedSoFar(accounts.getTotLoanIssuedSoFar());
        accountsDto.setTotalOutstandingAmountPayableToBank(accounts.getTotalOutStandingAmountPayableToBank());
        accountsDto.setAccountStatus(accounts.getAccountStatus());
        accountsDto.setListOfBeneficiary(accounts.getListOfBeneficiary());
        accountsDto.setListOfTransactions(accounts.getListOfTransactions());

        return accountsDto;
    }

    public static Customer mapToCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setCustomerId(customerDto.getCustomerId());
        customer.setName(customerDto.getCustomerName());
        customer.setDateOfBirth(customerDto.getDateOfBirth());
        customer.setAge(customerDto.getAge());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setAdharNumber(customerDto.getAdharNumber());
        customer.setPanNumber(customerDto.getPanNumber());
        customer.setVoterId(customerDto.getVoterId());
        customer.setDrivingLicense(customerDto.getDrivingLicense());
        customer.setPassportNumber(customerDto.getPassportNumber());
        List<Accounts> listOfAccounts=customerDto.getAccounts().stream().map(Mapper::mapToAccounts).collect(Collectors.toList());
        customer.setAccounts(listOfAccounts);
        return customer;
    }

    public static CustomerDto mapToCustomerDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(customer.getCustomerId());
        customerDto.setCustomerName(customer.getName());
        customerDto.setDateOfBirth(customer.getDateOfBirth());
        customerDto.setAge(customer.getAge());
        customerDto.setEmail(customer.getEmail());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setAdharNumber(customer.getAdharNumber());
        customerDto.setPanNumber(customer.getPanNumber());
        customerDto.setVoterId(customer.getVoterId());
        customerDto.setDrivingLicense(customer.getDrivingLicense());
        customerDto.setPassportNumber(customer.getPassportNumber());
        customerDto.setAccounts(customerDto.getAccounts());
        return customerDto;
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

    public static TransactionsDto mapToTransactionsDto(Transactions transactions) {
        TransactionsDto transactionsDto = new TransactionsDto();
        transactionsDto.setTransactionAmount(transactions.getTransactionAmount());
        transactionsDto.setTransactionTimeStamp(transactions.getTransactionTimeStamp());
        transactionsDto.setTransactionId(transactions.getTransactionId());
        transactionsDto.setTransactedAccountNumber(transactions.getTransactedAccountNumber());
        transactionsDto.setTransactionType(transactions.getTransactionType());
        transactionsDto.setDescription(transactions.getDescription());
        transactionsDto.setAccounts(transactions.getAccounts());
        return transactionsDto;
    }


    public static Accounts inputToAccounts(InputDto inputDto) {
        Accounts accounts = new Accounts();
        accounts.setAccountNumber(inputDto.getAccountNumber());
        accounts.setBalance(inputDto.getBalance());
        accounts.setAccountType(inputDto.getAccountType());
        accounts.setBranchCode(inputDto.getBranchCode());
        accounts.setHomeBranch(inputDto.getHomeBranch());
        accounts.setTransferLimitPerDay(inputDto.getTransferLimitPerDay());
        accounts.setCreditScore(inputDto.getCreditScore());
        accounts.setApprovedLoanLimitBasedOnCreditScore(inputDto.getApprovedLoanLimitBasedOnCreditScore());
        accounts.setAnyActiveLoans(inputDto.getAnyActiveLoans());
        accounts.setTotLoanIssuedSoFar(inputDto.getTotLoanIssuedSoFar());
        accounts.setTotalOutStandingAmountPayableToBank(inputDto.getTotalOutStandingAmountPayableToBank());
        accounts.setAccountStatus(inputDto.getAccountStatus());
        accounts.setListOfBeneficiary(inputDto.getListOfBeneficiary());
        accounts.setListOfTransactions(inputDto.getListOfTransactions());
        accounts.setCustomer(inputDto.getCustomer());
        return accounts;
    }

    public static AccountsDto inputToAccountsDto(InputDto inputDto) {
        AccountsDto accountsDto = new AccountsDto();
        accountsDto.setAccountNumber(inputDto.getAccountNumber());
        accountsDto.setBalance(inputDto.getBalance());
        accountsDto.setAccountType(inputDto.getAccountType());
        accountsDto.setBranchCode(inputDto.getBranchCode());
        accountsDto.setHomeBranch(inputDto.getHomeBranch());
        accountsDto.setTransferLimitPerDay(inputDto.getTransferLimitPerDay());
        accountsDto.setCreditScore(inputDto.getCreditScore());
        accountsDto.setUpdateRequest(inputDto.getUpdateRequest());
        accountsDto.setApprovedLoanLimitBasedOnCreditScore(inputDto.getApprovedLoanLimitBasedOnCreditScore());
        accountsDto.setAnyActiveLoans(inputDto.getAnyActiveLoans());
        accountsDto.setTotLoanIssuedSoFar(inputDto.getTotLoanIssuedSoFar());
        accountsDto.setTotalOutstandingAmountPayableToBank(inputDto.getTotalOutStandingAmountPayableToBank());
        accountsDto.setAccountStatus(inputDto.getAccountStatus());
        accountsDto.setListOfBeneficiary(inputDto.getListOfBeneficiary());
        accountsDto.setListOfTransactions(inputDto.getListOfTransactions());
        return accountsDto;
    }

    public static CustomerDto inputToCustomerDto(InputDto inputDto) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(inputDto.getCustomerId());


        customerDto.setCustomerName(inputDto.getName());


        //converting date to its desired type
        if (null != inputDto.getDateOfBirthInYMD()) {
            String date[] = inputDto.getDateOfBirthInYMD().split("-");
            LocalDate dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
            customerDto.setDateOfBirth(dob);
        }

        customerDto.setAge(inputDto.getAge());
        customerDto.setEmail(inputDto.getEmail());
        customerDto.setPhoneNumber(inputDto.getPhoneNumber());
        customerDto.setAdharNumber(inputDto.getAdharNumber());
        customerDto.setPanNumber(inputDto.getPanNumber());
        customerDto.setVoterId(inputDto.getVoterId());
        customerDto.setDrivingLicense(inputDto.getDrivingLicense());
        customerDto.setPassportNumber(inputDto.getPassportNumber());
        return customerDto;
    }

    public static Customer inputToCustomer(InputDto inputDto) {
        Customer customer = new Customer();
        customer.setName(inputDto.getName());

        //converting date to its desired type
        if (null != inputDto.getDateOfBirthInYMD()) {
            String date[] = inputDto.getDateOfBirthInYMD().split("-");
            LocalDate dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
            customer.setDateOfBirth(dob);
        }
        customer.setEmail(inputDto.getEmail());
        customer.setPhoneNumber(inputDto.getPhoneNumber());
        customer.setAdharNumber(inputDto.getAdharNumber());
        customer.setPanNumber(inputDto.getPanNumber());
        customer.setVoterId(inputDto.getVoterId());
        customer.setDrivingLicense(inputDto.getDrivingLicense());
        customer.setPassportNumber(inputDto.getPassportNumber());
        return customer;
    }

    public static InputDto mapToinputDto(Customer customer, Accounts accounts) {
        InputDto inputDto = new InputDto();

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
        inputDto.setApprovedLoanLimitBasedOnCreditScore(accounts.getApprovedLoanLimitBasedOnCreditScore());
        inputDto.setAnyActiveLoans(accounts.getAnyActiveLoans());
        inputDto.setTotLoanIssuedSoFar(accounts.getTotLoanIssuedSoFar());
        inputDto.setTotalOutStandingAmountPayableToBank(accounts.getTotalOutStandingAmountPayableToBank());
        inputDto.setAccountStatus(accounts.getAccountStatus());
        inputDto.setListOfBeneficiary(accounts.getListOfBeneficiary());
        inputDto.setListOfTransactions(accounts.getListOfTransactions());
        inputDto.setCustomer(accounts.getCustomer());
        return inputDto;
    }

    public static CustomerOutPutDto mapToCustomerOutputDto(CustomerDto customerDto){
        CustomerOutPutDto customerOutPutDto=new CustomerOutPutDto();
        customerOutPutDto.setCustomerId(customerDto.getCustomerId());
        customerOutPutDto.setCustomerName(customerDto.getCustomerName());
        customerOutPutDto.setDateOfBirth(customerDto.getDateOfBirth());
        customerOutPutDto.setAge(customerDto.getAge());
        customerOutPutDto.setEmail(customerDto.getEmail());
        customerOutPutDto.setPhoneNumber(customerDto.getPhoneNumber());
        customerOutPutDto.setAdharNumber(customerDto.getAdharNumber());
        customerOutPutDto.setPanNumber(customerDto.getPanNumber());
        customerOutPutDto.setVoterId(customerDto.getVoterId());
        customerOutPutDto.setDrivingLicense(customerDto.getDrivingLicense());
        customerOutPutDto.setPassportNumber(customerDto.getPassportNumber());
       return customerOutPutDto;
    }

    public static AccountsOutPutDto mapToAccountsOutputDto(AccountsDto accountsDto){
        AccountsOutPutDto accountsOutPutDto=new AccountsOutPutDto();
        accountsOutPutDto.setAccountNumber(accountsDto.getAccountNumber());
        accountsOutPutDto.setBalance(accountsDto.getBalance());
        accountsOutPutDto.setAccountType(accountsDto.getAccountType());
        accountsOutPutDto.setBranchCode(accountsDto.getBranchCode());
        accountsOutPutDto.setHomeBranch(accountsDto.getHomeBranch());
        accountsOutPutDto.setTransferLimitPerDay(accountsDto.getTransferLimitPerDay());
        accountsOutPutDto.setCreditScore(accountsDto.getCreditScore());
        accountsOutPutDto.setApprovedLoanLimitBasedOnCreditScore(accountsDto.getApprovedLoanLimitBasedOnCreditScore());
        accountsOutPutDto.setAnyActiveLoans(accountsDto.getAnyActiveLoans());
        accountsOutPutDto.setTotLoanIssuedSoFar(accountsDto.getTotLoanIssuedSoFar());
        accountsOutPutDto.setTotalOutstandingAmountPayableToBank(accountsDto.getTotalOutstandingAmountPayableToBank());
        accountsOutPutDto.setAccountStatus(accountsDto.getAccountStatus());
        accountsOutPutDto.setListOfBeneficiary(accountsDto.getListOfBeneficiary());
        accountsOutPutDto.setListOfTransactions(accountsDto.getListOfTransactions());
        return accountsOutPutDto;
    }

    public static OutputDto mapToOutPut(CustomerDto customerDto,AccountsDto accountsDto) {
        OutputDto outputDto = new OutputDto();
        outputDto.setCustomer(Mapper.mapToCustomerOutputDto(customerDto));
        outputDto.setAccounts(Mapper.mapToAccountsOutputDto(accountsDto));
        return outputDto;
    }

}
