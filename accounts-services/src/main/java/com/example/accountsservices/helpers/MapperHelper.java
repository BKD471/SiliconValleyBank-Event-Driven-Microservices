package com.example.accountsservices.helpers;

import com.example.accountsservices.dto.baseDtos.AccountsDto;
import com.example.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.example.accountsservices.dto.baseDtos.CustomerDto;
import com.example.accountsservices.dto.baseDtos.TransactionsDto;
import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.dto.outputDtos.AccountsOutPutDto;
import com.example.accountsservices.dto.outputDtos.CustomerOutPutDto;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.model.Transactions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapperHelper {
    /**
     * @param accountsDto
     * @paramType AccountsDto
     * @ReturnType Accounts
     */
    public static Accounts mapToAccounts(final AccountsDto accountsDto) {
        return  Accounts.builder()
                .accountType(accountsDto.getAccountType())
                .homeBranch(accountsDto.getHomeBranch()).build();
    }

    /**
     * @param accounts
     * @paramType Accounts
     * @ReturnType AccountsDto
     */
    public static AccountsDto mapToAccountsDto(final Accounts accounts) {
        List<BeneficiaryDto> beneficiaryDtoList=new ArrayList<>();
        List<TransactionsDto> transactionsDtoList=new ArrayList<>();
        if(null!=accounts.getListOfBeneficiary() && accounts.getListOfBeneficiary().size()>0){
            beneficiaryDtoList=accounts.getListOfBeneficiary().stream().map(MapperHelper::mapToBeneficiaryDto)
                            .collect(Collectors.toList());
        }
        if(null!=accounts.getListOfTransactions() && accounts.getListOfTransactions().size()>0){
            transactionsDtoList=accounts.getListOfTransactions().stream().map(MapperHelper::mapToTransactionsDto)
                    .collect(Collectors.toList());
        }

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
                .listOfBeneficiary(beneficiaryDtoList)
                .listOfTransactions(transactionsDtoList)
                .build();
    }

    public static Customer mapToCustomer(final CustomerDto customerDto) {
        List<Accounts> listOfAccounts = new ArrayList<>();
        if (null!=customerDto.getAccounts() && customerDto.getAccounts().size()>0) {
            listOfAccounts = customerDto.getAccounts().stream().
                    map(MapperHelper::mapToAccounts).collect(Collectors.toList());
        }
        return Customer.builder()
                .customerId(customerDto.getCustomerId())
                .name(customerDto.getCustomerName())
                .DateOfBirth(customerDto.getDateOfBirth())
                .age(customerDto.getAge())
                .email(customerDto.getEmail())
                //.password(customerDto.getPassword())
                .phoneNumber(customerDto.getPhoneNumber())
                .adharNumber(customerDto.getAdharNumber())
                .panNumber(customerDto.getPanNumber())
                .voterId(customerDto.getVoterId())
                .address(customerDto.getAddress())
                .imageName(customerDto.getImageName())
                .drivingLicense(customerDto.getDrivingLicense())
                .passportNumber(customerDto.getPassportNumber())
                .accounts(listOfAccounts)
                .build();
    }

    public static CustomerDto mapToCustomerDto(final Customer customer) {
        List<AccountsDto> listOfAccounts = new ArrayList<>();
        if (null!=customer.getAccounts() && customer.getAccounts().size()>0) {
            listOfAccounts = customer.getAccounts().stream().
                    map(MapperHelper::mapToAccountsDto).collect(Collectors.toList());
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
                .address(customer.getAddress())
                .imageName(customer.getImageName())
                .drivingLicense(customer.getDrivingLicense())
                .passportNumber(customer.getPassportNumber())
                .accounts(listOfAccounts)
                .build();
    }

    public static BeneficiaryDto mapToBeneficiaryDto(final Beneficiary beneficiary) {
        return BeneficiaryDto.builder()
                .beneficiaryId(beneficiary.getBeneficiaryId())
                .beneficiaryName(beneficiary.getBeneficiaryName())
                .benAge(beneficiary.getBenAge())
                .beneficiaryAccountNumber(beneficiary.getBeneficiaryAccountNumber())
                .bankCode(beneficiary.getBankCode())
                .benBank(beneficiary.getBenBank())
                .benPhoneNumber(beneficiary.getBenPhoneNumber())
                .benDrivingLicense(beneficiary.getBenDrivingLicense())
                .beneficiaryEmail(beneficiary.getBeneficiaryEmail())
                .benAdharNumber(beneficiary.getBenAdharNumber())
                .relation(beneficiary.getRelation())
                .benVoterId(beneficiary.getBenVoterId())
                .benPanNumber(beneficiary.getBenPanNumber())
                .benPassportNumber(beneficiary.getBenPassportNumber())
                .BenDate_Of_Birth(beneficiary.getBenDate_Of_Birth())
                .imageName(beneficiary.getImageName())
                .address(beneficiary.getAddress())
                .build();
    }

    public static Beneficiary mapToBeneficiary(final BeneficiaryDto beneficiaryDto) {
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
                .imageName(beneficiaryDto.getImageName())
                .address(beneficiaryDto.getAddress())
                .BenDate_Of_Birth(beneficiaryDto.getBenDate_Of_Birth())
                .build();
    }

    public static Transactions mapToTransactions(final TransactionsDto transactionsDto) {
        return Transactions.builder()
                .transactionAmount(transactionsDto.getTransactionAmount())
                .transactedAccountNumber(transactionsDto.getTransactedAccountNumber())
                .transactionType(transactionsDto.getTransactionType())
                .description(transactionsDto.getDescription())
                .build();
    }

    public static TransactionsDto mapToTransactionsDto(final Transactions transactions) {
        return TransactionsDto.builder()
                .transactionId(transactions.getTransactionId())
                .transactionAmount(transactions.getTransactionAmount())
                .transactionTimeStamp(transactions.getTransactionTimeStamp())
                .transactedAccountNumber(transactions.getTransactedAccountNumber())
                .transactionType(transactions.getTransactionType())
                .description(transactions.getDescription())
                .accountNumber(transactions.getAccounts().getAccountNumber())
                .build();
    }


    public static Accounts inputToAccounts(final PostInputRequestDto postInputRequestDto) {
        List<Beneficiary> beneficiaryList=new ArrayList<>();
        List<Transactions> transactionList=new ArrayList<>();
        if(null!=postInputRequestDto.getListOfBeneficiary() && postInputRequestDto.getListOfBeneficiary().size()>0){
            beneficiaryList=postInputRequestDto.getListOfBeneficiary().stream().map(MapperHelper::mapToBeneficiary)
                    .collect(Collectors.toList());
        }
        if(null!=postInputRequestDto.getListOfTransactions() && postInputRequestDto.getListOfTransactions().size()>0){
            transactionList=postInputRequestDto.getListOfTransactions().stream().map(MapperHelper::mapToTransactions)
                    .collect(Collectors.toList());
        }
        return Accounts.builder()
                .accountNumber(postInputRequestDto.getAccountNumber())
                .balance(postInputRequestDto.getBalance())
                .accountType(postInputRequestDto.getAccountType())
                .branchCode(postInputRequestDto.getBranchCode())
                .homeBranch(postInputRequestDto.getHomeBranch())
                .transferLimitPerDay(postInputRequestDto.getTransferLimitPerDay())
                .creditScore(postInputRequestDto.getCreditScore())
                .approvedLoanLimitBasedOnCreditScore(postInputRequestDto.getApprovedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(postInputRequestDto.getAnyActiveLoans())
                .totLoanIssuedSoFar(postInputRequestDto.getTotLoanIssuedSoFar())
                .totalOutStandingAmountPayableToBank(postInputRequestDto.getTotalOutStandingAmountPayableToBank())
                .accountStatus(postInputRequestDto.getAccountStatus())
                .listOfBeneficiary(beneficiaryList)
                .listOfTransactions(transactionList)
                .customer(postInputRequestDto.getCustomer())
                .build();
    }

    public static AccountsDto inputToAccountsDto(final PostInputRequestDto postInputRequestDto) {

        return AccountsDto.builder()
                .accountNumber(postInputRequestDto.getAccountNumber())
                .balance(postInputRequestDto.getBalance())
                .accountType(postInputRequestDto.getAccountType())
                .branchCode(postInputRequestDto.getBranchCode())
                .homeBranch(postInputRequestDto.getHomeBranch())
                .transferLimitPerDay(postInputRequestDto.getTransferLimitPerDay())
                .creditScore(postInputRequestDto.getCreditScore())
                .updateRequest(postInputRequestDto.getUpdateRequest())
                .approvedLoanLimitBasedOnCreditScore(postInputRequestDto.getApprovedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(postInputRequestDto.getAnyActiveLoans())
                .totLoanIssuedSoFar(postInputRequestDto.getTotLoanIssuedSoFar())
                .totalOutstandingAmountPayableToBank(postInputRequestDto.getTotalOutStandingAmountPayableToBank())
                .accountStatus(postInputRequestDto.getAccountStatus())
                .listOfBeneficiary(postInputRequestDto.getListOfBeneficiary())
                .listOfTransactions(postInputRequestDto.getListOfTransactions())
                .build();
    }

    public static AccountsDto deleteRequestInputToAccountsDto(final DeleteInputRequestDto deleteInputRequestDto) {
        List<BeneficiaryDto> beneficiaryDtoList=new ArrayList<>();
        List<TransactionsDto> transactionsDtoList=new ArrayList<>();
        if(null!=deleteInputRequestDto.getListOfBeneficiary() && deleteInputRequestDto.getListOfBeneficiary().size()>0){
            beneficiaryDtoList=deleteInputRequestDto.getListOfBeneficiary().stream().map(MapperHelper::mapToBeneficiaryDto)
                    .collect(Collectors.toList());
        }
        if(null!=deleteInputRequestDto.getListOfTransactions() && deleteInputRequestDto.getListOfTransactions().size()>0){
            transactionsDtoList=deleteInputRequestDto.getListOfTransactions().stream().map(MapperHelper::mapToTransactionsDto)
                    .collect(Collectors.toList());
        }

        return AccountsDto.builder()
                .accountNumber(deleteInputRequestDto.getAccountNumber())
                .balance(deleteInputRequestDto.getBalance())
                .accountType(deleteInputRequestDto.getAccountType())
                .branchCode(deleteInputRequestDto.getBranchCode())
                .homeBranch(deleteInputRequestDto.getHomeBranch())
                .transferLimitPerDay(deleteInputRequestDto.getTransferLimitPerDay())
                .creditScore(deleteInputRequestDto.getCreditScore())
                .updateRequest(deleteInputRequestDto.getUpdateRequest())
                .approvedLoanLimitBasedOnCreditScore(deleteInputRequestDto.getApprovedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(deleteInputRequestDto.getAnyActiveLoans())
                .totLoanIssuedSoFar(deleteInputRequestDto.getTotLoanIssuedSoFar())
                .totalOutstandingAmountPayableToBank(deleteInputRequestDto.getTotalOutStandingAmountPayableToBank())
                .accountStatus(deleteInputRequestDto.getAccountStatus())
                .listOfBeneficiary(beneficiaryDtoList)
                .listOfTransactions(transactionsDtoList)
                .build();
    }

    public static AccountsDto putInputRequestToAccountsDto(final PutInputRequestDto putInputRequestDto) {
        List<BeneficiaryDto> beneficiaryDtoList=new ArrayList<>();
        List<TransactionsDto> transactionsDtoList=new ArrayList<>();
        if(null!=putInputRequestDto.getListOfBeneficiary() && putInputRequestDto.getListOfBeneficiary().size()>0){
            beneficiaryDtoList=putInputRequestDto.getListOfBeneficiary().stream().map(MapperHelper::mapToBeneficiaryDto)
                    .collect(Collectors.toList());
        }
        if(null!=putInputRequestDto.getListOfTransactions() && putInputRequestDto.getListOfTransactions().size()>0){
            transactionsDtoList=putInputRequestDto.getListOfTransactions().stream().map(MapperHelper::mapToTransactionsDto)
                    .collect(Collectors.toList());
        }

        return AccountsDto.builder()
                .accountNumber(putInputRequestDto.getAccountNumber())
                .balance(putInputRequestDto.getBalance())
                .accountType(putInputRequestDto.getAccountType())
                .branchCode(putInputRequestDto.getBranchCode())
                .homeBranch(putInputRequestDto.getHomeBranch())
                .transferLimitPerDay(putInputRequestDto.getTransferLimitPerDay())
                .creditScore(putInputRequestDto.getCreditScore())
                .updateRequest(putInputRequestDto.getUpdateRequest())
                .approvedLoanLimitBasedOnCreditScore(putInputRequestDto.getApprovedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(putInputRequestDto.getAnyActiveLoans())
                .totLoanIssuedSoFar(putInputRequestDto.getTotLoanIssuedSoFar())
                .totalOutstandingAmountPayableToBank(putInputRequestDto.getTotalOutStandingAmountPayableToBank())
                .accountStatus(putInputRequestDto.getAccountStatus())
                .listOfBeneficiary(beneficiaryDtoList)
                .listOfTransactions(transactionsDtoList)
                .build();
    }

    public static AccountsDto getInputToAccountsDto(final GetInputRequestDto getInputRequestDto) {
        List<BeneficiaryDto> beneficiaryDtoList=new ArrayList<>();
        List<TransactionsDto> transactionsDtoList=new ArrayList<>();
        if(null!=getInputRequestDto.getListOfBeneficiary() && getInputRequestDto.getListOfBeneficiary().size()>0){
            beneficiaryDtoList=getInputRequestDto.getListOfBeneficiary().stream().map(MapperHelper::mapToBeneficiaryDto)
                    .collect(Collectors.toList());
        }
        if(null!=getInputRequestDto.getListOfTransactions() && getInputRequestDto.getListOfTransactions().size()>0){
            transactionsDtoList=getInputRequestDto.getListOfTransactions().stream().map(MapperHelper::mapToTransactionsDto)
                    .collect(Collectors.toList());
        }

        return AccountsDto.builder()
                .accountNumber(getInputRequestDto.getAccountNumber())
                .balance(getInputRequestDto.getBalance())
                .accountType(getInputRequestDto.getAccountType())
                .branchCode(getInputRequestDto.getBranchCode())
                .homeBranch(getInputRequestDto.getHomeBranch())
                .transferLimitPerDay(getInputRequestDto.getTransferLimitPerDay())
                .creditScore(getInputRequestDto.getCreditScore())
                .updateRequest(getInputRequestDto.getUpdateRequest())
                .approvedLoanLimitBasedOnCreditScore(getInputRequestDto.getApprovedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(getInputRequestDto.getAnyActiveLoans())
                .totLoanIssuedSoFar(getInputRequestDto.getTotLoanIssuedSoFar())
                .totalOutstandingAmountPayableToBank(getInputRequestDto.getTotalOutStandingAmountPayableToBank())
                .accountStatus(getInputRequestDto.getAccountStatus())
                .listOfBeneficiary(beneficiaryDtoList)
                .listOfTransactions(transactionsDtoList)
                .build();
    }

    public static CustomerDto inputToCustomerDto(final PostInputRequestDto postInputRequestDto) {
        LocalDate dob = null;
        //converting date to its desired type
        if (null != postInputRequestDto.getDateOfBirthInYYYYMMDD()) {
            final String[] date = postInputRequestDto.getDateOfBirthInYYYYMMDD().split("-");
            dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }

        return CustomerDto.builder()
                .customerId(postInputRequestDto.getCustomerId())
                .customerName(postInputRequestDto.getName())
                .DateOfBirth(dob)
                .age(postInputRequestDto.getAge())
                .email(postInputRequestDto.getEmail())
                //.password(postInputRequestDto.getPassword())
                .phoneNumber(postInputRequestDto.getPhoneNumber())
                .adharNumber(postInputRequestDto.getAdharNumber())
                .panNumber(postInputRequestDto.getPanNumber())
                .voterId(postInputRequestDto.getVoterId())
                .drivingLicense(postInputRequestDto.getDrivingLicense())
                .passportNumber(postInputRequestDto.getPassportNumber())
                .address(postInputRequestDto.getAddress())
                .imageName(postInputRequestDto.getImageName())
                .customerImage(postInputRequestDto.getCustomerImage())
                .build();
    }

    public static CustomerDto deleteInputRequestToCustomerDto(final DeleteInputRequestDto deleteInputRequestDto) {
        LocalDate dob = null;
        //converting date to its desired type
        if (null != deleteInputRequestDto.getDateOfBirthInYYYYMMDD()) {
            final String[] date = deleteInputRequestDto.getDateOfBirthInYYYYMMDD().split("-");
            dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }

        return CustomerDto.builder()
                .customerId(deleteInputRequestDto.getCustomerId())
                .customerName(deleteInputRequestDto.getName())
                .DateOfBirth(dob)
                .age(deleteInputRequestDto.getAge())
                .email(deleteInputRequestDto.getEmail())
                //.password(deleteInputRequestDto.getPassword())
                .phoneNumber(deleteInputRequestDto.getPhoneNumber())
                .adharNumber(deleteInputRequestDto.getAdharNumber())
                .panNumber(deleteInputRequestDto.getPanNumber())
                .voterId(deleteInputRequestDto.getVoterId())
                .drivingLicense(deleteInputRequestDto.getDrivingLicense())
                .passportNumber(deleteInputRequestDto.getPassportNumber())
                .address(deleteInputRequestDto.getAddress())
                .imageName(deleteInputRequestDto.getImageName())
                .build();
    }

    public static CustomerDto putInputRequestToCustomerDto(final PutInputRequestDto putInputRequestDto) {
        LocalDate dob = null;
        //converting date to its desired type
        if (null != putInputRequestDto.getDateOfBirthInYYYYMMDD()) {
            final String[] date = putInputRequestDto.getDateOfBirthInYYYYMMDD().split("-");
            dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }

        return CustomerDto.builder()
                .customerId(putInputRequestDto.getCustomerId())
                .customerName(putInputRequestDto.getName())
                .DateOfBirth(dob)
                .age(putInputRequestDto.getAge())
                .email(putInputRequestDto.getEmail())
                //.password(putInputRequestDto.getPassword())
                .phoneNumber(putInputRequestDto.getPhoneNumber())
                .adharNumber(putInputRequestDto.getAdharNumber())
                .panNumber(putInputRequestDto.getPanNumber())
                .voterId(putInputRequestDto.getVoterId())

                .drivingLicense(putInputRequestDto.getDrivingLicense())
                .passportNumber(putInputRequestDto.getPassportNumber())
                .imageName(putInputRequestDto.getImageName())
                .customerImage(putInputRequestDto.getCustomerImage())
                .address(putInputRequestDto.getAddress())
                .build();
    }

    public static CustomerDto getInputToCustomerDto(final GetInputRequestDto getInputRequestDto) {
        LocalDate dob = null;
        //converting date to its desired type
        if (null != getInputRequestDto.getDateOfBirthInYYYYMMDD()) {
            final String[] date = getInputRequestDto.getDateOfBirthInYYYYMMDD().split("-");
            dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }

        return CustomerDto.builder()
                .customerId(getInputRequestDto.getCustomerId())
                .customerName(getInputRequestDto.getName())
                .DateOfBirth(dob)
                .age(getInputRequestDto.getAge())
                //.password(getInputRequestDto.getPassword())
                .email(getInputRequestDto.getEmail())
                .phoneNumber(getInputRequestDto.getPhoneNumber())
                .adharNumber(getInputRequestDto.getAdharNumber())
                .panNumber(getInputRequestDto.getPanNumber())
                .voterId(getInputRequestDto.getVoterId())
                .drivingLicense(getInputRequestDto.getDrivingLicense())
                .passportNumber(getInputRequestDto.getPassportNumber())
                .imageName(getInputRequestDto.getImageName())
                .address(getInputRequestDto.getAddress())
                .build();
    }

    public static Customer inputToCustomer(final PostInputRequestDto postInputRequestDto) {
        //converting date to its desired type
        LocalDate dob = null;
        if (null != postInputRequestDto.getDateOfBirthInYYYYMMDD()) {
            final String[] date = postInputRequestDto.getDateOfBirthInYYYYMMDD().split("-");
            dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }

        return Customer.builder()
                .name(postInputRequestDto.getName())
                .DateOfBirth(dob)
                .email(postInputRequestDto.getEmail())
                .password(postInputRequestDto.getPassword())
                .phoneNumber(postInputRequestDto.getPhoneNumber())
                .adharNumber(postInputRequestDto.getAdharNumber())
                .panNumber(postInputRequestDto.getPanNumber())
                .voterId(postInputRequestDto.getVoterId())
                .drivingLicense(postInputRequestDto.getDrivingLicense())
                .passportNumber(postInputRequestDto.getPassportNumber())
                .imageName(postInputRequestDto.getImageName())
                .address(postInputRequestDto.getAddress())
                .build();
    }

    public static BeneficiaryDto mapInputDtoToBenDto(final PostInputRequestDto postInputRequestDto) {
        //converting date to its desired type
        LocalDate dob = null;
        if (null != postInputRequestDto.getDateOfBirthInYYYYMMDD()) {
            final String[] date = postInputRequestDto.getDateOfBirthInYYYYMMDD().split("-");
            dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }
        return BeneficiaryDto.builder()
                .beneficiaryAccountNumber(postInputRequestDto.getBeneficiaryAccountNumber())
                .beneficiaryName(postInputRequestDto.getBeneficiaryName())
                .benAge(postInputRequestDto.getBenAge())
                .beneficiaryId(postInputRequestDto.getBeneficiaryId())
                .benPanNumber(postInputRequestDto.getPanNumber())
                .benPhoneNumber(postInputRequestDto.getPhoneNumber())
                .benAdharNumber(postInputRequestDto.getAdharNumber())
                .beneficiaryEmail(postInputRequestDto.getEmail())
                .benPassportNumber(postInputRequestDto.getPassportNumber())
                .benBank(postInputRequestDto.getBenBank())
                .benVoterId(postInputRequestDto.getVoterId())
                .BenDate_Of_Birth(dob)
                .benDrivingLicense(postInputRequestDto.getDrivingLicense())
                .benUpdateRequest(postInputRequestDto.getBenRequest())
                .relation(postInputRequestDto.getBloodRelation())
                .address(postInputRequestDto.getAddress())
                .imageName(postInputRequestDto.getImageName())
                .build();
    }


    public static BeneficiaryDto mapDeleteInputRequestDtoToBenDto(final DeleteInputRequestDto deleteInputRequestDto) {
        //converting date to its desired type
        LocalDate dob = null;
        if (null != deleteInputRequestDto.getBen_date_of_birthINYYYYMMDD()) {
            final String[] date = deleteInputRequestDto.getBen_date_of_birthINYYYYMMDD().split("-");
            dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }
        return BeneficiaryDto.builder()
                .beneficiaryAccountNumber(deleteInputRequestDto.getBeneficiaryAccountNumber())
                .beneficiaryName(deleteInputRequestDto.getBeneficiaryName())
                .benAge(deleteInputRequestDto.getBenAge())
                .beneficiaryId(deleteInputRequestDto.getBeneficiaryId())
                .benPanNumber(deleteInputRequestDto.getBenPanNumber())
                .benPhoneNumber(deleteInputRequestDto.getBenPhoneNumber())
                .benAdharNumber(deleteInputRequestDto.getBenAdharNumber())
                .beneficiaryEmail(deleteInputRequestDto.getEmail())
                .benPassportNumber(deleteInputRequestDto.getBenPassportNumber())
                .benBank(deleteInputRequestDto.getBenBank())
                .benVoterId(deleteInputRequestDto.getBenVoterId())
                .BenDate_Of_Birth(dob)
                .benDrivingLicense(deleteInputRequestDto.getBenDrivingLicense())
                .benUpdateRequest(deleteInputRequestDto.getBenRequest())
                .relation(deleteInputRequestDto.getBloodRelation())
                .address(deleteInputRequestDto.getAddress())
                .imageName(deleteInputRequestDto.getImageName())
                .build();
    }

    public static BeneficiaryDto mapPutInputRequestDtoToBenDto(final PutInputRequestDto putInputRequestDto) {
        //converting date to its desired type
        LocalDate dob = null;
        if (null != putInputRequestDto.getBen_date_of_birthInYYYYMMDD()) {
            final String[] date = putInputRequestDto.getBen_date_of_birthInYYYYMMDD().split("-");
            dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }
        return BeneficiaryDto.builder()
                .beneficiaryAccountNumber(putInputRequestDto.getBeneficiaryAccountNumber())
                .beneficiaryName(putInputRequestDto.getBeneficiaryName())
                .benAge(putInputRequestDto.getBenAge())
                .beneficiaryId(putInputRequestDto.getBeneficiaryId())
                .benPanNumber(putInputRequestDto.getBenPanNumber())
                .benPhoneNumber(putInputRequestDto.getBenPhoneNumber())
                .benAdharNumber(putInputRequestDto.getBenAdharNumber())
                .beneficiaryEmail(putInputRequestDto.getBeneficiaryEmail())
                .benPassportNumber(putInputRequestDto.getBenPassportNumber())
                .benBank(putInputRequestDto.getBenBank())
                .benVoterId(putInputRequestDto.getBenVoterId())
                .BenDate_Of_Birth(dob)
                .benDrivingLicense(putInputRequestDto.getBenDrivingLicense())
                .benUpdateRequest(putInputRequestDto.getBenRequest())
                .address(putInputRequestDto.getAddress())
                .imageName(putInputRequestDto.getImageName())
                .relation(putInputRequestDto.getBloodRelation())
                .build();
    }

    public static BeneficiaryDto mapGetRequestInputDtoToBenDto(final GetInputRequestDto getInputRequestDto) {
        //converting date to its desired type
        LocalDate dob = null;
        if (null != getInputRequestDto.getBen_date_of_birthINYYYYMMDD()) {
            final String[] date = getInputRequestDto.getBen_date_of_birthINYYYYMMDD().split("-");
            dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }
        return BeneficiaryDto.builder()
                .beneficiaryAccountNumber(getInputRequestDto.getBeneficiaryAccountNumber())
                .beneficiaryName(getInputRequestDto.getBeneficiaryName())
                .benAge(getInputRequestDto.getBenAge())
                .beneficiaryId(getInputRequestDto.getBeneficiaryId())
                .benPanNumber(getInputRequestDto.getBenPanNumber())
                .benPhoneNumber(getInputRequestDto.getBenPhoneNumber())
                .benAdharNumber(getInputRequestDto.getBenAdharNumber())
                .beneficiaryEmail(getInputRequestDto.getEmail())
                .benPassportNumber(getInputRequestDto.getBenPassportNumber())
                .benBank(getInputRequestDto.getBenBank())
                .benVoterId(getInputRequestDto.getBenVoterId())
                .BenDate_Of_Birth(dob)
                .address(getInputRequestDto.getAddress())
                .imageName(getInputRequestDto.getImageName())
                .benDrivingLicense(getInputRequestDto.getBenDrivingLicense())
                .benUpdateRequest(getInputRequestDto.getBenRequest())
                .relation(getInputRequestDto.getBloodRelation())
                .build();
    }

    public static CustomerOutPutDto mapToCustomerOutputDto(final CustomerDto customerDto) {
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
                .address(customerDto.getAddress())
                .imageName(customerDto.getImageName())
                .build();
    }

    public static AccountsOutPutDto mapToAccountsOutputDto(final AccountsDto accountsDto) {
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
}
