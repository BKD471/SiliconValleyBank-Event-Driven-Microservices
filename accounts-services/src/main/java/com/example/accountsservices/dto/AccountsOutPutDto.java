package com.example.accountsservices.dto;

import com.example.accountsservices.model.Accounts;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountsOutPutDto {
    private Long accountNumber;
    private Long balance;
    private Accounts.Branch homeBranch;
    private Accounts.AccountType accountType;
    private Accounts.AccountStatus accountStatus;
    private String branchCode;
    private Long transferLimitPerDay;
    private int creditScore;
    private Long approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private Long totalOutstandingAmountPayableToBank;
    private Long totLoanIssuedSoFar;

    private List<BeneficiaryDto> listOfBeneficiary;
    private List<TransactionsDto> listOfTransactions;
}
