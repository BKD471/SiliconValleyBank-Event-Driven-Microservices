package com.example.accountsservices.dto;

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
    private Accounts.AccountType accountType;
    private Accounts.AccountStatus accountStatus;
    private String branchCode;
    private Accounts.Branch homeBranch;
    private long transferLimitPerDay;
    private int creditScore;
    private long approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private long totalOutstandingAmountPayableToBank;
    private long totLoanIssuedSoFar;
    @JsonIgnore
    @JsonProperty(value = "updateRequest")
    private UpdateRequest updateRequest;
    public enum UpdateRequest {
        CREATE_ACC,
        ADD_ACCOUNT,
        LEND_LOAN,
        UPDATE_HOME_BRANCH,
        UPDATE_CREDIT_SCORE,
        UPDATE_CUSTOMER_DETAILS,
        UPLOAD_CUSTOMER_IMAGE,
        GET_CUSTOMER_IMAGE,
        GET_CREDIT_SCORE,
        GET_ACC_INFO,
        GET_ALL_ACC,
        BLOCK_ACC,
        CLOSE_ACC,
        RE_OPEN_ACC,
        INC_TRANSFER_LIMIT,
        INC_APPROVED_LOAN_LIMIT,
        DELETE_ACC,
        DELETE_ALL_ACC
    }

    private List<BeneficiaryDto> listOfBeneficiary;
    private List<TransactionsDto> listOfTransactions;
}
