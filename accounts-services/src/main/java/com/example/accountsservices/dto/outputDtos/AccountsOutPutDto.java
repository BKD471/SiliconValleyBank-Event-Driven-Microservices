package com.example.accountsservices.dto.outputDtos;

import com.example.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.example.accountsservices.dto.baseDtos.TransactionsDto;
import com.example.accountsservices.helpers.AllConstantHelpers;
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
    private AllConstantHelpers.Branch homeBranch;
    private AllConstantHelpers.AccountType accountType;
    private AllConstantHelpers.AccountStatus accountStatus;
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
