package com.siliconvalley.accountsservices.helpers;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record TransactionsInvoicableObject( Timestamp transactionTimeStamp,
         String transactionId,
         BigDecimal transactionAmount,
         String transactedAccountNumber,
         String transactionType,
         String description,
         BigDecimal balance){


    public static final class Builder{
        private Timestamp transactionTimeStamp;
        private String transactionId;
        private BigDecimal transactionAmount;
        private String transactedAccountNumber;
        private String transactionType;
        private String description;
        private BigDecimal balance;

        public Builder(){}

        public Builder transactionTimeStamp(Timestamp transactionTimeStamp){
            this.transactionTimeStamp=transactionTimeStamp;
            return this;
        }
        public Builder transactionId(String transactionId){
            this.transactionId=transactionId;
            return this;
        }
        public Builder transactionAmount(BigDecimal transactionAmount){
            this.transactionAmount=transactionAmount;
            return this;
        }
        public Builder transactedAccountNumber(String transactedAccountNumber){
            this.transactedAccountNumber=transactedAccountNumber;
            return this;
        }
        public Builder transactionType(String transactionType){
            this.transactionType=transactionType;
            return this;
        }
        public Builder description(String description){
            this.description=description;
            return this;
        }
        public Builder balance(BigDecimal balance){
            this.balance=balance;
            return this;
        }

        public TransactionsInvoicableObject build(){
            return new TransactionsInvoicableObject(transactionTimeStamp,transactionId,transactionAmount,
                    transactedAccountNumber,transactionType,description,balance);
        }
    }
}
