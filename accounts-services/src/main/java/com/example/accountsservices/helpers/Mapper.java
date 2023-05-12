package com.example.accountsservices.helpers;

import com.example.accountsservices.dto.*;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.model.Transactions;

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
        return Accounts.builder()
                .accountType(accountsDto.getAccountType())
                .homeBranch(accountsDto.getHomeBranch()).build();
    }

    /**
     * @param accounts
     * @paramType Accounts
     * @ReturnType AccountsDto
     */
    public static AccountsDto mapToAccountsDto(Accounts accounts) {
        return AccountsDto.builder()
                .accountNumber(accounts.getAccountNumber())
                .balance(accounts.getBalance())
                .accountType(accounts.getAccountType())
                .branchCode(accounts.getBranchCode())
                .homeBranch(accounts.getHomeBranch())
                .transferLimitPerDay(accounts.getTransferLimitPerDay())
                .creditScore(accounts.getCreditScore())
                .approvedLoanLimitBasedOnCreditScore(accounts.getApprovedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(accounts.getAnyActiveLoans())
                .totLoanIssuedSoFar(accounts.getTotLoanIssuedSoFar())
                .totalOutstandingAmountPayableToBank(accounts.getTotalOutStandingAmountPayableToBank())
                .accountStatus(accounts.getAccountStatus())
                .listOfBeneficiary(accounts.getListOfBeneficiary())
                .listOfTransactions(accounts.getListOfTransactions())
                .build();
    }

    public static Customer mapToCustomer(CustomerDto customerDto) {
        List<Accounts> listOfAccounts = new ArrayList<>();
        if (customerDto.getAccounts() != null) {
            listOfAccounts = customerDto.getAccounts().stream().
                    map(Mapper::mapToAccounts).collect(Collectors.toList());
        }
        return Customer.builder()
                .customerId(customerDto.getCustomerId())
                .name(customerDto.getCustomerName())
                .DateOfBirth(customerDto.getDateOfBirth())
                .age(customerDto.getAge())
                .email(customerDto.getEmail())
                .phoneNumber(customerDto.getPhoneNumber())
                .adharNumber(customerDto.getAdharNumber())
                .panNumber(customerDto.getPanNumber())
                .voterId(customerDto.getVoterId())
                .drivingLicense(customerDto.getDrivingLicense())
                .passportNumber(customerDto.getPassportNumber())
                .accounts(listOfAccounts)
                .build();
    }

    public static CustomerDto mapToCustomerDto(Customer customer) {
        List<AccountsDto> listOfAccounts = new ArrayList<>();
        if (customer.getAccounts() != null) {
            listOfAccounts = customer.getAccounts().stream().
                    map(Mapper::mapToAccountsDto).collect(Collectors.toList());
        }
        return CustomerDto.builder()
                .customerId(customer.getCustomerId())
                .customerName(customer.getName())
                .DateOfBirth(customer.getDateOfBirth())
                .age(customer.getAge())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .adharNumber(customer.getAdharNumber())
                .panNumber(customer.getPanNumber())
                .voterId(customer.getVoterId())
                .drivingLicense(customer.getDrivingLicense())
                .passportNumber(customer.getPassportNumber())
                .accounts(listOfAccounts)
                .build();
    }

    public static BeneficiaryDto mapToBeneficiaryDto(Beneficiary beneficiary) {
        return BeneficiaryDto.builder()
                .beneficiaryId(beneficiary.getBeneficiaryId())
                .beneficiaryName(beneficiary.getBeneficiaryName())
                .benAge(beneficiary.getBenAge())
                .beneficiaryAccountNumber(beneficiary.getBeneficiaryAccountNumber())
                .bankCode(beneficiary.getBankCode())
                .benBank(beneficiary.getBenBank())
                .beneficiaryEmail(beneficiary.getBeneficiaryEmail())
                .benAdharNumber(beneficiary.getBenAdharNumber())
                .relation(beneficiary.getRelation())
                .benVoterId(beneficiary.getBenVoterId())
                .benPanNumber(beneficiary.getBenPanNumber())
                .benPassportNumber(beneficiary.getBenPassportNumber())
                .BenDate_Of_Birth(beneficiary.getBenDate_Of_Birth())
                .build();
    }

    public static Beneficiary mapToBeneficiary(BeneficiaryDto beneficiaryDto) {
        return Beneficiary.builder()
                .beneficiaryName(beneficiaryDto.getBeneficiaryName())
                .beneficiaryAccountNumber(beneficiaryDto.getBeneficiaryAccountNumber())
                .benBank(beneficiaryDto.getBenBank())
                .benAdharNumber(beneficiaryDto.getBenAdharNumber())
                .relation(beneficiaryDto.getRelation())
                .benPhoneNumber(beneficiaryDto.getBenPhoneNumber())
                .beneficiaryEmail(beneficiaryDto.getBeneficiaryEmail())
                .benVoterId(beneficiaryDto.getBenVoterId())
                .benPanNumber(beneficiaryDto.getBenPanNumber())
                .benDrivingLicense(beneficiaryDto.getBenDrivingLicense())
                .benPassportNumber(beneficiaryDto.getBenPassportNumber())
                .BenDate_Of_Birth(beneficiaryDto.getBenDate_Of_Birth())
                .build();
    }

    public static Transactions mapToTransactions(TransactionsDto transactionsDto) {
        return Transactions.builder()
                .transactionAmount(transactionsDto.getTransactionAmount())
                .transactedAccountNumber(transactionsDto.getTransactedAccountNumber())
                .transactionType(transactionsDto.getTransactionType())
                .description(transactionsDto.getDescription())
                .accounts(transactionsDto.getAccounts())
                .build();
    }

    public static TransactionsDto mapToTransactionsDto(Transactions transactions) {
        return TransactionsDto.builder()
                .transactionId(transactions.getTransactionId())
                .transactionAmount(transactions.getTransactionAmount())
                .transactionTimeStamp(transactions.getTransactionTimeStamp())
                .transactedAccountNumber(transactions.getTransactedAccountNumber())
                .transactionType(transactions.getTransactionType())
                .description(transactions.getDescription())
                .accounts(transactions.getAccounts())
                .build();
    }


    public static Accounts inputToAccounts(InputDto inputDto) {
        return Accounts.builder()
                .accountNumber(inputDto.getAccountNumber())
                .balance(inputDto.getBalance())
                .accountType(inputDto.getAccountType())
                .branchCode(inputDto.getBranchCode())
                .homeBranch(inputDto.getHomeBranch())
                .transferLimitPerDay(inputDto.getTransferLimitPerDay())
                .creditScore(inputDto.getCreditScore())
                .approvedLoanLimitBasedOnCreditScore(inputDto.getApprovedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(inputDto.getAnyActiveLoans())
                .totLoanIssuedSoFar(inputDto.getTotLoanIssuedSoFar())
                .totalOutStandingAmountPayableToBank(inputDto.getTotalOutStandingAmountPayableToBank())
                .accountStatus(inputDto.getAccountStatus())
                .listOfBeneficiary(inputDto.getListOfBeneficiary())
                .listOfTransactions(inputDto.getListOfTransactions())
                .customer(inputDto.getCustomer())
                .build();
    }

    public static AccountsDto inputToAccountsDto(InputDto inputDto) {
        return AccountsDto.builder()
                .accountNumber(inputDto.getAccountNumber())
                .balance(inputDto.getBalance())
                .accountType(inputDto.getAccountType())
                .branchCode(inputDto.getBranchCode())
                .homeBranch(inputDto.getHomeBranch())
                .transferLimitPerDay(inputDto.getTransferLimitPerDay())
                .creditScore(inputDto.getCreditScore())
                .updateRequest(inputDto.getUpdateRequest())
                .approvedLoanLimitBasedOnCreditScore(inputDto.getApprovedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(inputDto.getAnyActiveLoans())
                .totLoanIssuedSoFar(inputDto.getTotLoanIssuedSoFar())
                .totalOutstandingAmountPayableToBank(inputDto.getTotalOutStandingAmountPayableToBank())
                .accountStatus(inputDto.getAccountStatus())
                .listOfBeneficiary(inputDto.getListOfBeneficiary())
                .listOfTransactions(inputDto.getListOfTransactions())
                .build();
    }

    public static CustomerDto inputToCustomerDto(InputDto inputDto) {
        LocalDate dob = null;
        //converting date to its desired type
        if (null != inputDto.getDateOfBirthInYYYYMMDD()) {
            String[] date = inputDto.getDateOfBirthInYYYYMMDD().split("-");
            dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }

        return CustomerDto.builder()
                .customerId(inputDto.getCustomerId())
                .customerName(inputDto.getName())
                .DateOfBirth(dob)
                .age(inputDto.getAge())
                .email(inputDto.getEmail())
                .phoneNumber(inputDto.getPhoneNumber())
                .adharNumber(inputDto.getAdharNumber())
                .panNumber(inputDto.getPanNumber())
                .voterId(inputDto.getVoterId())
                .drivingLicense(inputDto.getDrivingLicense())
                .passportNumber(inputDto.getPassportNumber())
                .build();
    }

    public static Customer inputToCustomer(InputDto inputDto) {
        //converting date to its desired type
        LocalDate dob = null;
        if (null != inputDto.getDateOfBirthInYYYYMMDD()) {
            String[] date = inputDto.getDateOfBirthInYYYYMMDD().split("-");
            dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }

        return Customer.builder()
                .name(inputDto.getName())
                .DateOfBirth(dob)
                .email(inputDto.getEmail())
                .phoneNumber(inputDto.getPhoneNumber())
                .adharNumber(inputDto.getAdharNumber())
                .panNumber(inputDto.getPanNumber())
                .voterId(inputDto.getVoterId())
                .drivingLicense(inputDto.getDrivingLicense())
                .passportNumber(inputDto.getPassportNumber())
                .build();
    }

    public static BeneficiaryDto mapInputDtoToBenDto(InputDto inputDto) {
        //converting date to its desired type
        LocalDate dob = null;
        if (null != inputDto.getBen_date_of_birthINYYYYMMDD()) {
            String[] date = inputDto.getBen_date_of_birthINYYYYMMDD().split("-");
            dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }
        return BeneficiaryDto.builder()
                .beneficiaryAccountNumber(inputDto.getBeneficiaryAccountNumber())
                .beneficiaryName(inputDto.getBeneficiaryName())
                .benAge(inputDto.getBenAge())
                .beneficiaryId(inputDto.getBeneficiaryId())
                .benPanNumber(inputDto.getBenPanNumber())
                .benPhoneNumber(inputDto.getBenPhoneNumber())
                .benAdharNumber(inputDto.getBenAdharNumber())
                .beneficiaryEmail(inputDto.getEmail())
                .benPassportNumber(inputDto.getBenPassportNumber())
                .benBank(inputDto.getBenBank())
                .benVoterId(inputDto.getBenVoterId())
                .BenDate_Of_Birth(dob)
                .benDrivingLicense(inputDto.getBenDrivingLicense())
                .benUpdateRequest(inputDto.getBenRequest())
                .relation(inputDto.getBloodRelation())
                .build();
    }

    public static InputDto mapToinputDto(Customer customer, Accounts accounts) {
        return InputDto.builder()
                //setting customer info
                .name(customer.getName())
                .dateOfBirthInYYYYMMDD(customer.getDateOfBirth().toString())
                .age(customer.getAge())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .adharNumber(customer.getAdharNumber())
                .panNumber(customer.getPanNumber())
                .voterId(customer.getVoterId())
                .drivingLicense(customer.getDrivingLicense())
                .passportNumber(customer.getPassportNumber())

                //setting account info
                .accountNumber(accounts.getAccountNumber())
                .balance(accounts.getBalance())
                .accountType(accounts.getAccountType())
                .branchCode(accounts.getBranchCode())
                .homeBranch(accounts.getHomeBranch())
                .transferLimitPerDay(accounts.getTransferLimitPerDay())
                .creditScore(accounts.getCreditScore())
                .approvedLoanLimitBasedOnCreditScore(accounts.getApprovedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(accounts.getAnyActiveLoans())
                .totLoanIssuedSoFar(accounts.getTotLoanIssuedSoFar())
                .totalOutStandingAmountPayableToBank(accounts.getTotalOutStandingAmountPayableToBank())
                .accountStatus(accounts.getAccountStatus())
                .listOfBeneficiary(accounts.getListOfBeneficiary())
                .listOfTransactions(accounts.getListOfTransactions())
                .customer(accounts.getCustomer())
                .build();
    }

    public static CustomerOutPutDto mapToCustomerOutputDto(CustomerDto customerDto) {
        return CustomerOutPutDto.builder()
                .customerId(customerDto.getCustomerId())
                .customerName(customerDto.getCustomerName())
                .DateOfBirth(customerDto.getDateOfBirth())
                .age(customerDto.getAge())
                .email(customerDto.getEmail())
                .phoneNumber(customerDto.getPhoneNumber())
                .adharNumber(customerDto.getAdharNumber())
                .panNumber(customerDto.getPanNumber())
                .voterId(customerDto.getVoterId())
                .drivingLicense(customerDto.getDrivingLicense())
                .passportNumber(customerDto.getPassportNumber())
                .build();
    }

    public static AccountsOutPutDto mapToAccountsOutputDto(AccountsDto accountsDto) {
        return AccountsOutPutDto.builder()
                .accountNumber(accountsDto.getAccountNumber())
                .balance(accountsDto.getBalance())
                .accountType(accountsDto.getAccountType())
                .branchCode(accountsDto.getBranchCode())
                .homeBranch(accountsDto.getHomeBranch())
                .transferLimitPerDay(accountsDto.getTransferLimitPerDay())
                .creditScore(accountsDto.getCreditScore())
                .approvedLoanLimitBasedOnCreditScore(accountsDto.getApprovedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(accountsDto.getAnyActiveLoans())
                .totLoanIssuedSoFar(accountsDto.getTotLoanIssuedSoFar())
                .totalOutstandingAmountPayableToBank(accountsDto.getTotalOutstandingAmountPayableToBank())
                .accountStatus(accountsDto.getAccountStatus())
                .listOfBeneficiary(accountsDto.getListOfBeneficiary())
                .listOfTransactions(accountsDto.getListOfTransactions())
                .build();
    }

    public static OutputDto mapToOutPutDto(CustomerDto customerDto, AccountsDto accountsDto, String message) {
        return OutputDto.builder()
                .defaultMessage(message)
                .customer(Mapper.mapToCustomerOutputDto(customerDto))
                .accounts(Mapper.mapToAccountsOutputDto(accountsDto))
                .build();
    }

    public static OutputDto customerDtoToOutPut(CustomerDto customerDto) {
        return OutputDto.builder()
                .customer(Mapper.mapToCustomerOutputDto(customerDto))
                .build();
    }

    public static OutputDto accountsDtoToOutPut(AccountsDto accountsDto) {
        return OutputDto.builder()
                .accounts(Mapper.mapToAccountsOutputDto(accountsDto))
                .build();
    }

}
