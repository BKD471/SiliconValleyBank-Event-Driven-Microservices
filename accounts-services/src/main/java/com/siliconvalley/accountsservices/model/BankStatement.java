package com.siliconvalley.accountsservices.model;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class BankStatement {
    private String accountName;
    private String accountNumber;
    private AllConstantHelpers.Branch branch;
    private AllConstantHelpers.AccountType accountType;
    private Double RateOfInterest;
    private BigDecimal balance;

    @Override
    public String toString() {
       return String.format("<-------------------------------------------------------------------------------------------------> \n" +
                "accountName= %s \n accountNumber= %s \n branch= %s \n accountType= %s " +
               "\n RateOfInterest= %s \n balance= %s",accountName,accountNumber,branch,accountType,RateOfInterest,balance);
    }
}
