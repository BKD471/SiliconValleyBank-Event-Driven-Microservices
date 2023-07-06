package com.example.accountsservices.dto.baseDtos;

import com.example.accountsservices.helpers.AllConstantHelpers;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class AccountsDto {
    private final String accountNumber;
    private final long balance;
    private final AllConstantHelpers.AccountType accountType;
    private final AllConstantHelpers.AccountStatus accountStatus;
    private final String branchCode;
    private final AllConstantHelpers.Branch homeBranch;
    private final long transferLimitPerDay;
    private final int creditScore;
    private final long approvedLoanLimitBasedOnCreditScore;
    private final Boolean anyActiveLoans;
    private final long totalOutstandingAmountPayableToBank;
    private final long totLoanIssuedSoFar;
    @JsonIgnore
    @JsonProperty(value = "updateRequest")
    private final AllConstantHelpers.UpdateRequest updateRequest;

    private final List<BeneficiaryDto> listOfBeneficiary;
    private final List<TransactionsDto> listOfTransactions;
}
