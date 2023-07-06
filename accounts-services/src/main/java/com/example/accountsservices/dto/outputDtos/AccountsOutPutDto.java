package com.example.accountsservices.dto.outputDtos;

import com.example.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.example.accountsservices.dto.baseDtos.TransactionsDto;
import com.example.accountsservices.helpers.AllConstantHelpers;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class AccountsOutPutDto {
    private final String accountNumber;
    private final long balance;
    private final AllConstantHelpers.Branch homeBranch;
    private final AllConstantHelpers.AccountType accountType;
    private final AllConstantHelpers.AccountStatus accountStatus;
    private final String branchCode;
    private final long transferLimitPerDay;
    private final int creditScore;
    private final long approvedLoanLimitBasedOnCreditScore;
    private final Boolean anyActiveLoans;
    private final long totalOutstandingAmountPayableToBank;
    private final long totLoanIssuedSoFar;
    private final List<BeneficiaryDto> listOfBeneficiary;
    private final List<TransactionsDto> listOfTransactions;
}
