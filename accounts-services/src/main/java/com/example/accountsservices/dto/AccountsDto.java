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
        ADD_ACCOUNT,
        LEND_LOAN,
        CHANGE_HOME_BRANCH,
        UPDATE_CREDIT_SCORE,
        BLOCK_ACC,
        INC_TRANSFER_LIMIT,
        INC_APPROVED_LOAN_LIMIT,
        GET_CREDIT_SCORE,
        GET_ACC_INFO,
        GET_ALL_ACC,
        DELETE_ACC,
        DELETE_ALL_ACC
    }

    private List<Beneficiary> listOfBeneficiary;
    private List<Transactions> listOfTransactions;
}
