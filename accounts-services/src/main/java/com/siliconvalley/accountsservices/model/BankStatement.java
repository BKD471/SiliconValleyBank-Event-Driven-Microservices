package com.siliconvalley.accountsservices.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Builder
public final class BankStatement {
    private String accountName;
    private String accountNumber;
    private String branch;
    private String accountType;
    private Double rateOfInterest;
    private BigDecimal balance;
    private Date date;
    private List<Transactions> listOfTransaction;

    public BankStatement(String accountName, String accountNumber, String branch, String accountType,
                         Double rateOfInterest, BigDecimal balance, Date date,
                         List<Transactions> listOfTransaction) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.accountType = accountType;
        this.rateOfInterest = rateOfInterest;
        this.balance = balance;
        this.date = date;
        this.listOfTransaction = listOfTransaction;
    }

    @Override
    public String toString() {
       return String.format("<-------------------------------------------------------------------------------------------------> \n" +
                "accountName= %s \n accountNumber= %s \n branch= %s \n accountType= %s " +
               "\n RateOfInterest= %s \n balance= %s \n transactionList= %s",accountName,accountNumber,branch,accountType,rateOfInterest,balance,listOfTransaction);
    }
}
