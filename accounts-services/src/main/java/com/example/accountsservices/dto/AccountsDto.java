package com.example.accountsservices.dto;

import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.model.Transactions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountsDto extends AbstractParentDto {
    private Long accountNumber;
    private Long balance;
    private Accounts.AccountType accountType;
    private Accounts.AccountStatus accountStatus;
    private String branchCode;
    private Accounts.Branch homeBranch;
    private Long transferLimitPerDay;
    private int creditScore;
    private Long approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private Long totalOutstandingAmountPayableToBank;
    private Long totLoanIssuedSoFar;
    @JsonIgnore
    @JsonProperty(value = "updateRequest")
    private UpdateRequest updateRequest;
    public enum UpdateRequest {
        CREATE_ACC,
        CHANGE_HOME_BRANCH,
        ADD_ACCOUNT,
        GET_CREDIT_SCORE,
        UPDATE_CREDIT_SCORE,
        DELETE_ACC,
        DELETE_ALL_ACC,
        BLOCK_ACC,
        INC_TRANSFER_LIMIT,
        INC_APPROVED_LOAN_LIMIT,LEND_LOAN
    }

    private List<Beneficiary> listOfBeneficiary;
    private List<Transactions> listOfTransactions;
}
