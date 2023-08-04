package com.siliconvalley.accountsservices.dto.baseDtos;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountsDto {
    private String accountNumber;
    private BigDecimal balance;
    private AllConstantHelpers.AccountType accountType;
    private AllConstantHelpers.AccountStatus accountStatus;
    private String branchCode;
    private AllConstantHelpers.Branch homeBranch;
    private BigDecimal transferLimitPerDay;
    private int creditScore;
    private BigDecimal approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private BigDecimal totalOutstandingAmountPayableToBank;
    private BigDecimal totLoanIssuedSoFar;
    @JsonIgnore
    @JsonProperty(value = "updateRequest")
    private AllConstantHelpers.UpdateRequest updateRequest;

    private List<BeneficiaryDto> listOfBeneficiary;
    private List<TransactionsDto> listOfTransactions;
}
