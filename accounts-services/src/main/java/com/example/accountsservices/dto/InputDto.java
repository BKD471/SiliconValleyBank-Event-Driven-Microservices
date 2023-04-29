package com.example.accountsservices.dto;

import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.model.Transactions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InputDto extends BaseDto {
    private String name;
    private Long customerId;
    private String dateOfBirthInYMD;
    private Accounts.AccountType accountType;
    private Accounts.Branch homeBranch;



    //    @JsonIgnore
    private Long accountNumber;
    //   @JsonIgnore
    private Long balance;
    // @JsonIgnore
    private AccountsDto.UpdateRequest updateRequest;
    private String branchCode;
    //    @JsonIgnore
    private Long transferLimitPerDay;
    //   @JsonIgnore
    private int creditScore;
    //    @JsonIgnore
    private Accounts.AccountStatus accountStatus;
    private Long approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private Long totLoanIssuedSoFar;
    private Long totalOutStandingAmountPayableToBank;
    //   @JsonIgnore
    private List<Beneficiary> listOfBeneficiary = new ArrayList<>();
    //   @JsonIgnore
    private List<Transactions> listOfTransactions = new ArrayList<>();
    private Customer customer;
    private List<Accounts> accounts;
}
