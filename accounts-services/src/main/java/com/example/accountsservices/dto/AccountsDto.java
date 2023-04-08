package com.example.accountsservices.dto;


import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Transactions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountsDto extends BaseDto {
    private Long accountNumber;
    private Long customerId;
    private Long balance;
    private  Long transactionAmount;
    private Accounts.AccountType accountType;
    private Accounts.AccountStatus accountStatus;
    private String branchAddress;
    private List<Beneficiary> listOfBeneficiary;
    private List<Transactions> listOfTransactions;
}
