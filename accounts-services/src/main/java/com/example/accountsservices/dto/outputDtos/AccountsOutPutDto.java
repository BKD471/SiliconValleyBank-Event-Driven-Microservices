package com.example.accountsservices.dto.outputDtos;

import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.model.Accounts;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountsOutPutDto {
    private long accountNumber;
    private long balance;
    private Accounts.Branch homeBranch;
    private Accounts.AccountType accountType;
    private Accounts.AccountStatus accountStatus;
    private String branchCode;
    private long transferLimitPerDay;
    private int creditScore;
    private long approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private long totalOutstandingAmountPayableToBank;
    private long totLoanIssuedSoFar;
    private List<BeneficiaryDto> listOfBeneficiary;
    private List<TransactionsDto> listOfTransactions;
}
