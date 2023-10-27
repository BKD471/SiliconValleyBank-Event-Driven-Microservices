package com.siliconvalley.accountsservices.helpers;

import com.siliconvalley.accountsservices.dto.baseDtos.AccountsDto;
import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.siliconvalley.accountsservices.dto.outputDtos.AccountsOutPutDto;
import com.siliconvalley.accountsservices.dto.outputDtos.CustomerOutPutDto;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Beneficiary;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.model.Transactions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

public class MapperHelper {
    /**
     * @param accountsDto
     * @paramType AccountsDto
     * @ReturnType Accounts
     */
    public static Accounts mapToAccounts(final AccountsDto accountsDto) {
        return Accounts.builder()
                .accountType(accountsDto.accountType())
                .homeBranch(accountsDto.homeBranch())
                .creditScore(accountsDto.creditScore())
                .build();
    }

    public static LocalDate dateParserInYYYYMMDD(String dateInString){
        LocalDate dob = null;
        //converting date to its desired type
        if (isNotBlank(dateInString)) {
            final String[] date = dateInString.split("-");
            dob = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }
        return dob;
    }

    /**
     * @param accounts
     * @paramType Accounts
     * @ReturnType AccountsDto
     */
    public static AccountsDto mapToAccountsDto(final Accounts accounts) {
        Set<BeneficiaryDto> beneficiaryDtoList=new LinkedHashSet<>();
        Set<TransactionsDto> transactionsDtoList=new LinkedHashSet<>();
        if(!isEmpty(accounts.getListOfBeneficiary())){
            beneficiaryDtoList=accounts.getListOfBeneficiary().stream().map(MapperHelper::mapToBeneficiaryDto)
                            .collect(Collectors.toSet());
        }
        if(!isEmpty(accounts.getListOfTransactions())){
            transactionsDtoList=accounts.getListOfTransactions().stream().map(MapperHelper::mapToTransactionsDto)
                    .collect(Collectors.toSet());
        }

        return new AccountsDto.Builder()
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
        Set<Accounts> listOfAccounts = new LinkedHashSet<>();
        if (!isEmpty(customerDto.accounts())) {
            listOfAccounts = customerDto.accounts().stream().
                    map(MapperHelper::mapToAccounts).collect(Collectors.toSet());
        }
        return Customer.builder()
                .customerId(customerDto.customerId())
                .name(customerDto.customerName())
                .DateOfBirth(customerDto.DateOfBirth())
                .age(customerDto.age())
                .email(customerDto.email())
                .phoneNumber(customerDto.phoneNumber())
                .adharNumber(customerDto.adharNumber())
                .panNumber(customerDto.panNumber())
                .voterId(customerDto.voterId())
                .address(customerDto.address())
                .imageName(customerDto.imageName())
                .drivingLicense(customerDto.drivingLicense())
                .passportNumber(customerDto.passportNumber())
                .password(customerDto.password())
                .accounts(listOfAccounts)
                .build();
    }

    public static CustomerDto mapToCustomerDto(final Customer customer) {
        Set<AccountsDto> listOfAccounts = new LinkedHashSet<>();
        if (!isEmpty(customer.getAccounts())) {
            listOfAccounts = customer.getAccounts().stream().
                    map(MapperHelper::mapToAccountsDto).collect(Collectors.toSet());
        }
        return new CustomerDto.Builder()
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
        return new BeneficiaryDto.Builder()
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
                .beneficiaryName(beneficiaryDto.beneficiaryName())
                .beneficiaryAccountNumber(beneficiaryDto.beneficiaryAccountNumber())
                .benBank(beneficiaryDto.benBank())
                .benAdharNumber(beneficiaryDto.benAdharNumber())
                .relation(beneficiaryDto.relation())
                .benPhoneNumber(beneficiaryDto.benPhoneNumber())
                .beneficiaryEmail(beneficiaryDto.beneficiaryEmail())
                .benVoterId(beneficiaryDto.benVoterId())
                .benPanNumber(beneficiaryDto.benPanNumber())
                .benDrivingLicense(beneficiaryDto.benDrivingLicense())
                .benPassportNumber(beneficiaryDto.benPassportNumber())
                .imageName(beneficiaryDto.imageName())
                .address(beneficiaryDto.address())
                .BenDate_Of_Birth(beneficiaryDto.BenDate_Of_Birth())
                .benAge(beneficiaryDto.benAge())
                .build();
    }

    public static Transactions mapToTransactions(final TransactionsDto transactionsDto) {
        return Transactions.builder()
                .transactionAmount(transactionsDto.transactionAmount())
                .transactedAccountNumber(transactionsDto.transactedAccountNumber())
                .transactionType(transactionsDto.transactionType())
                .description(transactionsDto.description())
                .build();
    }

    public static TransactionsDto mapToTransactionsDto(final Transactions transactions) {
        return new TransactionsDto.Builder()
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
        Set<Beneficiary> beneficiaryList=new LinkedHashSet<>();
        Set<Transactions> transactionList=new LinkedHashSet<>();
        if(!isEmpty(postInputRequestDto.listOfBeneficiary())){
            beneficiaryList=postInputRequestDto.listOfBeneficiary().stream().map(MapperHelper::mapToBeneficiary)
                    .collect(Collectors.toSet());
        }
        if(!isEmpty(postInputRequestDto.listOfTransactions())){
            transactionList=postInputRequestDto.listOfTransactions().stream().map(MapperHelper::mapToTransactions)
                    .collect(Collectors.toSet());
        }
        return Accounts.builder()
                .accountNumber(postInputRequestDto.accountNumber())
                .balance(postInputRequestDto.balance())
                .accountType(postInputRequestDto.accountType())
                .branchCode(postInputRequestDto.branchCode())
                .homeBranch(postInputRequestDto.homeBranch())
                .transferLimitPerDay(postInputRequestDto.transferLimitPerDay())
                .creditScore(postInputRequestDto.creditScore())
                .approvedLoanLimitBasedOnCreditScore(postInputRequestDto.approvedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(postInputRequestDto.anyActiveLoans())
                .totLoanIssuedSoFar(postInputRequestDto.totLoanIssuedSoFar())
                .totalOutStandingAmountPayableToBank(postInputRequestDto.totalOutStandingAmountPayableToBank())
                .accountStatus(postInputRequestDto.accountStatus())
                .listOfBeneficiary(beneficiaryList)
                .listOfTransactions(transactionList)
                .customer(postInputRequestDto.customer())
                .build();
    }

    public static AccountsDto inputToAccountsDto(final PostInputRequestDto postInputRequestDto) {
        return new AccountsDto.Builder()
                .accountNumber(postInputRequestDto.accountNumber())
                .balance(postInputRequestDto.balance())
                .accountType(postInputRequestDto.accountType())
                .branchCode(postInputRequestDto.branchCode())
                .homeBranch(postInputRequestDto.homeBranch())
                .transferLimitPerDay(postInputRequestDto.transferLimitPerDay())
                .creditScore(postInputRequestDto.creditScore())
                .updateRequest(postInputRequestDto.updateRequest())
                .approvedLoanLimitBasedOnCreditScore(postInputRequestDto.approvedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(postInputRequestDto.anyActiveLoans())
                .totLoanIssuedSoFar(postInputRequestDto.totLoanIssuedSoFar())
                .totalOutstandingAmountPayableToBank(postInputRequestDto.totalOutStandingAmountPayableToBank())
                .accountStatus(postInputRequestDto.accountStatus())
                .listOfBeneficiary(postInputRequestDto.listOfBeneficiary())
                .listOfTransactions(postInputRequestDto.listOfTransactions())
                .build();
    }

    public static AccountsDto deleteRequestInputToAccountsDto(final DeleteInputRequestDto deleteInputRequestDto) {
        Set<BeneficiaryDto> beneficiaryDtoList=new LinkedHashSet<>();
        Set<TransactionsDto> transactionsDtoList=new LinkedHashSet<>();
        if(!isEmpty(deleteInputRequestDto.listOfBeneficiary())){
            beneficiaryDtoList=deleteInputRequestDto.listOfBeneficiary().stream().map(MapperHelper::mapToBeneficiaryDto)
                    .collect(Collectors.toSet());
        }
        if(!isEmpty(deleteInputRequestDto.listOfTransactions())){
            transactionsDtoList=deleteInputRequestDto.listOfTransactions().stream().map(MapperHelper::mapToTransactionsDto)
                    .collect(Collectors.toSet());
        }

        return new AccountsDto.Builder()
                .accountNumber(deleteInputRequestDto.accountNumber())
                .balance(deleteInputRequestDto.balance())
                .accountType(deleteInputRequestDto.accountType())
                .branchCode(deleteInputRequestDto.branchCode())
                .homeBranch(deleteInputRequestDto.homeBranch())
                .transferLimitPerDay(deleteInputRequestDto.transferLimitPerDay())
                .creditScore(deleteInputRequestDto.creditScore())
                .updateRequest(deleteInputRequestDto.updateRequest())
                .approvedLoanLimitBasedOnCreditScore(deleteInputRequestDto.approvedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(deleteInputRequestDto.anyActiveLoans())
                .totLoanIssuedSoFar(deleteInputRequestDto.totLoanIssuedSoFar())
                .totalOutstandingAmountPayableToBank(deleteInputRequestDto.totalOutStandingAmountPayableToBank())
                .accountStatus(deleteInputRequestDto.accountStatus())
                .listOfBeneficiary(beneficiaryDtoList)
                .listOfTransactions(transactionsDtoList)
                .build();
    }

    public static AccountsDto putInputRequestToAccountsDto(final PutInputRequestDto putInputRequestDto) {
        Set<BeneficiaryDto> beneficiaryDtoList=new LinkedHashSet<>();
        Set<TransactionsDto> transactionsDtoList=new LinkedHashSet<>();
        if(!isEmpty(putInputRequestDto.beneficiarySet())){
            beneficiaryDtoList=putInputRequestDto.beneficiarySet().stream().map(MapperHelper::mapToBeneficiaryDto)
                    .collect(Collectors.toSet());
        }
        if(!isEmpty(putInputRequestDto.transactionsSet())){
            transactionsDtoList=putInputRequestDto.transactionsSet().stream().map(MapperHelper::mapToTransactionsDto)
                    .collect(Collectors.toSet());
        }

        return new AccountsDto.Builder()
                .accountNumber(putInputRequestDto.accountNumber())
                .balance(putInputRequestDto.balance())
                .accountType(putInputRequestDto.accountType())
                .branchCode(putInputRequestDto.branchCode())
                .homeBranch(putInputRequestDto.homeBranch())
                .transferLimitPerDay(putInputRequestDto.transferLimitPerDay())
                .creditScore(putInputRequestDto.creditScore())
                .updateRequest(putInputRequestDto.updateRequest())
                .approvedLoanLimitBasedOnCreditScore(putInputRequestDto.approvedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(putInputRequestDto.anyActiveLoans())
                .totLoanIssuedSoFar(putInputRequestDto.totLoanIssuedSoFar())
                .totalOutstandingAmountPayableToBank(putInputRequestDto.totalOutStandingAmountPayableToBank())
                .accountStatus(putInputRequestDto.accountStatus())
                .listOfBeneficiary(beneficiaryDtoList)
                .listOfTransactions(transactionsDtoList)
                .build();
    }

    public static AccountsDto getInputToAccountsDto(final GetInputRequestDto getInputRequestDto) {
        Set<BeneficiaryDto> beneficiaryDtoList=new LinkedHashSet<>();
        Set<TransactionsDto> transactionsDtoList=new LinkedHashSet<>();
        if(!isEmpty(getInputRequestDto.listOfBeneficiary())){
            beneficiaryDtoList=getInputRequestDto.listOfBeneficiary().stream().map(MapperHelper::mapToBeneficiaryDto)
                    .collect(Collectors.toSet());
        }
        if(!isEmpty(getInputRequestDto.listOfTransactions())){
            transactionsDtoList=getInputRequestDto.listOfTransactions().stream().map(MapperHelper::mapToTransactionsDto)
                    .collect(Collectors.toSet());
        }

        return new AccountsDto.Builder()
                .accountNumber(getInputRequestDto.accountNumber())
                .balance(getInputRequestDto.balance())
                .accountType(getInputRequestDto.accountType())
                .branchCode(getInputRequestDto.branchCode())
                .homeBranch(getInputRequestDto.homeBranch())
                .transferLimitPerDay(getInputRequestDto.transferLimitPerDay())
                .creditScore(getInputRequestDto.creditScore())
                .updateRequest(getInputRequestDto.updateRequest())
                .approvedLoanLimitBasedOnCreditScore(getInputRequestDto.approvedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(getInputRequestDto.anyActiveLoans())
                .totLoanIssuedSoFar(getInputRequestDto.totLoanIssuedSoFar())
                .totalOutstandingAmountPayableToBank(getInputRequestDto.totalOutStandingAmountPayableToBank())
                .accountStatus(getInputRequestDto.accountStatus())
                .listOfBeneficiary(beneficiaryDtoList)
                .listOfTransactions(transactionsDtoList)
                .build();
    }

    public static CustomerDto inputToCustomerDto(final PostInputRequestDto postInputRequestDto) {
        return new CustomerDto.Builder()
                .customerId(postInputRequestDto.customerId())
                .customerName(postInputRequestDto.name())
                .DateOfBirth(dateParserInYYYYMMDD(postInputRequestDto.dateOfBirthInYYYYMMDD()))
                .age(postInputRequestDto.age())
                .email(postInputRequestDto.email())
                //.password(postInputRequestDto.getPassword())
                .phoneNumber(postInputRequestDto.phoneNumber())
                .adharNumber(postInputRequestDto.adharNumber())
                .panNumber(postInputRequestDto.panNumber())
                .voterId(postInputRequestDto.voterId())
                .drivingLicense(postInputRequestDto.drivingLicense())
                .passportNumber(postInputRequestDto.passportNumber())
                .address(postInputRequestDto.address())
                .imageName(postInputRequestDto.imageName())
                .customerImage(postInputRequestDto.customerImage())
                .build();
    }

    public static CustomerDto deleteInputRequestToCustomerDto(final DeleteInputRequestDto deleteInputRequestDto) {
        return new CustomerDto.Builder()
                .customerId(deleteInputRequestDto.customerId())
                .customerName(deleteInputRequestDto.name())
                .DateOfBirth(dateParserInYYYYMMDD(deleteInputRequestDto.dateOfBirthInYYYYMMDD()))
                .age(deleteInputRequestDto.age())
                .email(deleteInputRequestDto.email())
                .phoneNumber(deleteInputRequestDto.phoneNumber())
                .adharNumber(deleteInputRequestDto.adharNumber())
                .panNumber(deleteInputRequestDto.panNumber())
                .voterId(deleteInputRequestDto.voterId())
                .drivingLicense(deleteInputRequestDto.drivingLicense())
                .passportNumber(deleteInputRequestDto.passportNumber())
                .address(deleteInputRequestDto.address())
                .imageName(deleteInputRequestDto.imageName())
                .build();
    }

    public static CustomerDto putInputRequestToCustomerDto(final PutInputRequestDto putInputRequestDto) {
        return new CustomerDto.Builder()
                .customerId(putInputRequestDto.customerId())
                .customerName(putInputRequestDto.name())
                .DateOfBirth(dateParserInYYYYMMDD(putInputRequestDto.dateOfBirthInYYYYMMDD()))
                .age(putInputRequestDto.age())
                .email(putInputRequestDto.email())
                .phoneNumber(putInputRequestDto.phoneNumber())
                .adharNumber(putInputRequestDto.adharNumber())
                .panNumber(putInputRequestDto.panNumber())
                .voterId(putInputRequestDto.voterId())

                .drivingLicense(putInputRequestDto.drivingLicense())
                .passportNumber(putInputRequestDto.passportNumber())
                .imageName(putInputRequestDto.imageName())
                .customerImage(putInputRequestDto.customerImage())
                .address(putInputRequestDto.address())
                .build();
    }

    public static CustomerDto getInputToCustomerDto(final GetInputRequestDto getInputRequestDto) {
        return new CustomerDto.Builder()
                .customerId(getInputRequestDto.customerId())
                .customerName(getInputRequestDto.name())
                .DateOfBirth(dateParserInYYYYMMDD(getInputRequestDto.dateOfBirthInYYYYMMDD()))
                .age(getInputRequestDto.age())
                .email(getInputRequestDto.email())
                .phoneNumber(getInputRequestDto.phoneNumber())
                .adharNumber(getInputRequestDto.adharNumber())
                .panNumber(getInputRequestDto.panNumber())
                .voterId(getInputRequestDto.voterId())
                .drivingLicense(getInputRequestDto.drivingLicense())
                .passportNumber(getInputRequestDto.passportNumber())
                .imageName(getInputRequestDto.imageName())
                .address(getInputRequestDto.address())
                .build();
    }

    public static Customer inputToCustomer(final PostInputRequestDto postInputRequestDto) {
        return Customer.builder()
                .name(postInputRequestDto.name())
                .DateOfBirth(dateParserInYYYYMMDD(postInputRequestDto.dateOfBirthInYYYYMMDD()))
                .email(postInputRequestDto.email())
                .password(postInputRequestDto.password())
                .phoneNumber(postInputRequestDto.phoneNumber())
                .adharNumber(postInputRequestDto.adharNumber())
                .panNumber(postInputRequestDto.panNumber())
                .voterId(postInputRequestDto.voterId())
                .drivingLicense(postInputRequestDto.drivingLicense())
                .passportNumber(postInputRequestDto.passportNumber())
                .imageName(postInputRequestDto.imageName())
                .address(postInputRequestDto.address())
                .age(postInputRequestDto.age())
                .build();
    }

    public static BeneficiaryDto mapInputDtoToBenDto(final PostInputRequestDto postInputRequestDto) {
        return new BeneficiaryDto.Builder()
                .beneficiaryAccountNumber(postInputRequestDto.beneficiaryAccountNumber())
                .beneficiaryName(postInputRequestDto.beneficiaryName())
                .benAge(postInputRequestDto.benAge())
                .beneficiaryId(postInputRequestDto.beneficiaryId())
                .benPanNumber(postInputRequestDto.panNumber())
                .benPhoneNumber(postInputRequestDto.phoneNumber())
                .benAdharNumber(postInputRequestDto.adharNumber())
                .beneficiaryEmail(postInputRequestDto.email())
                .benPassportNumber(postInputRequestDto.passportNumber())
                .benBank(postInputRequestDto.benBank())
                .benVoterId(postInputRequestDto.voterId())
                .BenDate_Of_Birth(dateParserInYYYYMMDD(postInputRequestDto.dateOfBirthInYYYYMMDD()))
                .benDrivingLicense(postInputRequestDto.drivingLicense())
                .benUpdateRequest(postInputRequestDto.benRequest())
                .relation(postInputRequestDto.bloodRelation())
                .address(postInputRequestDto.address())
                .imageName(postInputRequestDto.imageName())
                .build();
    }


    public static BeneficiaryDto mapDeleteInputRequestDtoToBenDto(final DeleteInputRequestDto deleteInputRequestDto) {
        return new BeneficiaryDto.Builder()
                .beneficiaryAccountNumber(deleteInputRequestDto.beneficiaryAccountNumber())
                .beneficiaryName(deleteInputRequestDto.beneficiaryName())
                .benAge(deleteInputRequestDto.benAge())
                .beneficiaryId(deleteInputRequestDto.beneficiaryId())
                .benPanNumber(deleteInputRequestDto.benPanNumber())
                .benPhoneNumber(deleteInputRequestDto.benPhoneNumber())
                .benAdharNumber(deleteInputRequestDto.benAdharNumber())
                .beneficiaryEmail(deleteInputRequestDto.email())
                .benPassportNumber(deleteInputRequestDto.benPassportNumber())
                .benBank(deleteInputRequestDto.benBank())
                .benVoterId(deleteInputRequestDto.benVoterId())
                .BenDate_Of_Birth(dateParserInYYYYMMDD(deleteInputRequestDto.dateOfBirthInYYYYMMDD()))
                .benDrivingLicense(deleteInputRequestDto.benDrivingLicense())
                .benUpdateRequest(deleteInputRequestDto.benRequest())
                .relation(deleteInputRequestDto.bloodRelation())
                .address(deleteInputRequestDto.address())
                .imageName(deleteInputRequestDto.imageName())
                .build();
    }

    public static BeneficiaryDto mapPutInputRequestDtoToBenDto(final PutInputRequestDto putInputRequestDto) {
        return new BeneficiaryDto.Builder()
                .beneficiaryAccountNumber(putInputRequestDto.beneficiaryAccountNumber())
                .beneficiaryName(putInputRequestDto.beneficiaryName())
                .benAge(putInputRequestDto.benAge())
                .beneficiaryId(putInputRequestDto.beneficiaryId())
                .benPanNumber(putInputRequestDto.benPanNumber())
                .benPhoneNumber(putInputRequestDto.benPhoneNumber())
                .benAdharNumber(putInputRequestDto.benAdharNumber())
                .beneficiaryEmail(putInputRequestDto.beneficiaryEmail())
                .benPassportNumber(putInputRequestDto.benPassportNumber())
                .benBank(putInputRequestDto.benBank())
                .benVoterId(putInputRequestDto.benVoterId())
                .BenDate_Of_Birth(dateParserInYYYYMMDD(putInputRequestDto.ben_date_of_birthInYYYYMMDD()))
                .benDrivingLicense(putInputRequestDto.benDrivingLicense())
                .benUpdateRequest(putInputRequestDto.benRequest())
                .address(putInputRequestDto.address())
                .imageName(putInputRequestDto.imageName())
                .relation(putInputRequestDto.bloodRelation())
                .build();
    }

    public static BeneficiaryDto mapGetRequestInputDtoToBenDto(final GetInputRequestDto getInputRequestDto) {
        return new BeneficiaryDto.Builder()
                .beneficiaryAccountNumber(getInputRequestDto.beneficiaryAccountNumber())
                .beneficiaryName(getInputRequestDto.beneficiaryName())
                .benAge(getInputRequestDto.benAge())
                .beneficiaryId(getInputRequestDto.beneficiaryId())
                .benPanNumber(getInputRequestDto.benPanNumber())
                .benPhoneNumber(getInputRequestDto.benPhoneNumber())
                .benAdharNumber(getInputRequestDto.benAdharNumber())
                .beneficiaryEmail(getInputRequestDto.email())
                .benPassportNumber(getInputRequestDto.benPassportNumber())
                .benBank(getInputRequestDto.benBank())
                .benVoterId(getInputRequestDto.benVoterId())
                .BenDate_Of_Birth(dateParserInYYYYMMDD(getInputRequestDto.ben_date_of_birthINYYYYMMDD()))
                .address(getInputRequestDto.address())
                .imageName(getInputRequestDto.imageName())
                .benDrivingLicense(getInputRequestDto.benDrivingLicense())
                .benUpdateRequest(getInputRequestDto.benRequest())
                .relation(getInputRequestDto.bloodRelation())
                .build();
    }

    public static CustomerOutPutDto mapToCustomerOutputDto(final CustomerDto customerDto) {
        return new CustomerOutPutDto.Builder()
                .customerId(customerDto.customerId())
                .customerName(customerDto.customerName())
                .DateOfBirth(customerDto.DateOfBirth())
                .age(customerDto.age())
                .email(customerDto.email())
                .phoneNumber(customerDto.phoneNumber())
                .adharNumber(customerDto.adharNumber())
                .panNumber(customerDto.panNumber())
                .voterId(customerDto.voterId())
                .drivingLicense(customerDto.drivingLicense())
                .passportNumber(customerDto.passportNumber())
                .address(customerDto.address())
                .imageName(customerDto.imageName())
                .build();
    }

    public static AccountsOutPutDto mapToAccountsOutputDto(final AccountsDto accountsDto) {
        return new AccountsOutPutDto.Builder()
                .accountNumber(accountsDto.accountNumber())
                .balance(accountsDto.balance())
                .accountType(accountsDto.accountType())
                .branchCode(accountsDto.branchCode())
                .homeBranch(accountsDto.homeBranch())
                .transferLimitPerDay(accountsDto.transferLimitPerDay())
                .creditScore(accountsDto.creditScore())
                .approvedLoanLimitBasedOnCreditScore(accountsDto.approvedLoanLimitBasedOnCreditScore())
                .anyActiveLoans(accountsDto.anyActiveLoans())
                .totLoanIssuedSoFar(accountsDto.totLoanIssuedSoFar())
                .totalOutstandingAmountPayableToBank(accountsDto.totalOutstandingAmountPayableToBank())
                .accountStatus(accountsDto.accountStatus())
                .listOfBeneficiary(accountsDto.listOfBeneficiary())
                .listOfTransactions(accountsDto.listOfTransactions())
                .build();
    }

    //transactionTimeStamp   transactionId   transactionAmount
    // transactedAccountNumber  transactionType  description  balance
   public static TransactionsInvoicableObject mapToTransactionsInvoicableObject(Transactions transactions){
        return  TransactionsInvoicableObject.builder()
                .transactionId(transactions.getTransactionId())
                .transactionTimeStamp(convertLocalDateTimeToTimeStamp(transactions.getTransactionTimeStamp()))
                .transactionAmount(transactions.getTransactionAmount())
                .transactedAccountNumber(transactions.getTransactedAccountNumber())
                .transactionType(transactions.getTransactionType().toString())
                .description(transactions.getDescription().toString())
                .balance(transactions.getBalance())
                .build();
   }

   public static Timestamp convertLocalDateTimeToTimeStamp(LocalDateTime localDateTime){
        return Timestamp.valueOf(localDateTime);
   }

   public static LocalDateTime convertTimeStampToLocalDateTime(Timestamp timestamp){
        return timestamp.toLocalDateTime();
   }
    public static Date convertToUtilDate(LocalDate date){
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
