package com.example.accountsservices.dto;

import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
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
public class InputDto extends BaseDto{
    private String name;
    private String dateOfBirthInYMD;
    private Accounts.AccountType accountType;
    private Accounts.Branch homeBranch;
//    @JsonIgnore
    private Long accountNumber;
 //   @JsonIgnore
    private Long balance;
   // @JsonIgnore
    private String branchCode;
//    @JsonIgnore
    private Long transferLimitPerDay;
 //   @JsonIgnore
    private int creditScore;
//    @JsonIgnore
    private Accounts.AccountStatus accountStatus;
 //   @JsonIgnore
    private List<Beneficiary> listOfBeneficiary=new ArrayList<>();
 //   @JsonIgnore
    private List<Transactions> listOfTransactions=new ArrayList<>();
}
