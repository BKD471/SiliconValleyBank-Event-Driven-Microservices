package com.siliconvalley.accountsservices.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.Date;

@NoArgsConstructor
@Getter
@Builder
public final class BankStatement{
    private String accountName;
    private String accountNumber;
    private String branch;
    private String accountType;
    private Double rateOfInterest;
    private BigDecimal balance;
    private Set<Transactions> listOfTransaction;
    private LocalDate startDate;
    private LocalDate endDate;
    private Date date;

    public BankStatement(String accountName, String accountNumber, String branch,
                         String accountType, Double rateOfInterest,
                         BigDecimal balance, Set<Transactions> listOfTransaction,
                         LocalDate startDate,LocalDate endDate,Date date) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.accountType = accountType;
        this.rateOfInterest = rateOfInterest;
        this.balance = balance;
        this.listOfTransaction = listOfTransaction;
        this.startDate=startDate;
        this.endDate=endDate;
        this.date = date;
    }

    @Override
    public String toString() {
       return String.format("<-------------------------------------------------------------------------------------------------> \n" +
                "accountName= %s \n accountNumber= %s \n branch= %s \n accountType= %s " +
               "\n RateOfInterest= %s \n balance= %s \n transactionList= %s \n date= %s \n startDate= %s \n endDate= %s]",accountName,accountNumber,branch,accountType,rateOfInterest,balance,listOfTransaction,date,startDate,endDate);
    }
}
