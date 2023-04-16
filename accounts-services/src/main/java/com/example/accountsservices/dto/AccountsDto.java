package com.example.accountsservices.dto;

import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Transactions;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class AccountsDto extends AbstractParentDto {
    private Long accountNumber;
    private Long customerId;
    private Long balance;
    private Accounts.AccountType accountType;
    private Accounts.AccountStatus accountStatus;
    private  Long branchCode;
    private String homeBranch;
    private Long cashLimitPerDay;
    private int creditScore;
    private List<Beneficiary> listOfBeneficiary;
    private List<Transactions> listOfTransactions;
}
