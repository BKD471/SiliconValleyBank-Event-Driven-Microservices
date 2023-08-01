package com.siliconvalley.accountsservices.model;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;

import java.math.BigDecimal;

public class BankStatement {
    private String accountName;
    private String accountNumber;
    private AllConstantHelpers.Branch branch;
    private AllConstantHelpers.AccountType accountType;
    private Double RateOfInterest;
    private BigDecimal balance;
}
