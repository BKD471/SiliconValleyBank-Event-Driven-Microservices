package com.example.accountsservices.dto.baseDtos;

import com.example.accountsservices.helpers.AllConstantHelpers;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountsDto {
    private String accountNumber;
    private long balance;
    private AllConstantHelpers.AccountType accountType;
    private AllConstantHelpers.AccountStatus accountStatus;
    private String branchCode;
    private AllConstantHelpers.Branch homeBranch;
    private long transferLimitPerDay;
    private int creditScore;
    private long approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private long totalOutstandingAmountPayableToBank;
    private long totLoanIssuedSoFar;
    @JsonIgnore
    @JsonProperty(value = "updateRequest")
    private AllConstantHelpers.UpdateRequest updateRequest;

    private List<BeneficiaryDto> listOfBeneficiary;
    private List<TransactionsDto> listOfTransactions;
}
