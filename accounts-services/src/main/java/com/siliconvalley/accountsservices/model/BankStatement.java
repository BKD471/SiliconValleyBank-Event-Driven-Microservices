package com.siliconvalley.accountsservices.model;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class BankStatement {

    private final String companyName="SiliconValley Corporation Pvt Ltd";
    private final String city="Seattle";
    private final String street="562 1ST Ave S Ste 400";
    private final String ZipCode="98104-3816";
    private final String faxNumber="+1-907-555-1234";
    private final String State="Washington";
    private final String country="United States";

    private String accountName;
    private String accountNumber;
    private String branch;
    private String accountType;
    private Double RateOfInterest;
    private BigDecimal balance;
    private List<Transactions> listOfTransaction;

    @Override
    public String toString() {
       return String.format("<-------------------------------------------------------------------------------------------------> \n" +
                "accountName= %s \n accountNumber= %s \n branch= %s \n accountType= %s " +
               "\n RateOfInterest= %s \n balance= %s \n transactionList= %s",accountName,accountNumber,branch,accountType,RateOfInterest,balance,listOfTransaction);
    }
}
