package com.example.accountsservices.dto.baseDtos;

import com.example.accountsservices.helpers.AllEnumConstantHelpers;
import com.example.accountsservices.model.Accounts;
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
    private long accountNumber;
    private long balance;
    private AllEnumConstantHelpers.AccountType accountType;
    private AllEnumConstantHelpers.AccountStatus accountStatus;
    private String branchCode;
    private AllEnumConstantHelpers.Branch homeBranch;
    private long transferLimitPerDay;
    private int creditScore;
    private long approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private long totalOutstandingAmountPayableToBank;
    private long totLoanIssuedSoFar;
    @JsonIgnore
    @JsonProperty(value = "updateRequest")
    private AllEnumConstantHelpers.UpdateRequest updateRequest;

    private List<BeneficiaryDto> listOfBeneficiary;
    private List<TransactionsDto> listOfTransactions;
}
