package com.siliconvalley.accountsservices.helpers;

import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
public class TransactionsInvoicableObject {
    private Timestamp transactionTimeStamp;
    private String transactionId;
    private BigDecimal transactionAmount;
    private String transactedAccountNumber;
    private String transactionType;
    private String description;
    private BigDecimal balance;

    public TransactionsInvoicableObject() {
    }

    public TransactionsInvoicableObject(builder builder) {
        this.transactionTimeStamp = builder.transactionTimeStamp;
        this.transactionId = builder.transactionId;
        this.transactionAmount = builder.transactionAmount;
        this.transactedAccountNumber = builder.transactedAccountNumber;
        this.transactionType = builder.transactionType;
        this.description = builder.description;
        this.balance = builder.balance;
    }

    public static final class builder {
        private Timestamp transactionTimeStamp;
        private String transactionId;
        private BigDecimal transactionAmount;
        private String transactedAccountNumber;
        private String transactionType;
        private String description;
        private BigDecimal balance;

        public builder() {
        }

        public builder transactionTimeStamp(Timestamp transactionTimeStamp) {
            this.transactionTimeStamp = transactionTimeStamp;
            return this;
        }

        public builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public builder transactionAmount(BigDecimal transactionAmount) {
            this.transactionAmount = transactionAmount;
            return this;
        }

        public builder transactedAccountNumber(String transactedAccountNumber) {
            this.transactedAccountNumber = transactedAccountNumber;
            return this;
        }

        public builder transactionType(String transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public builder description(String description) {
            this.description = description;
            return this;
        }

        public builder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public TransactionsInvoicableObject build() {
            return new TransactionsInvoicableObject(this);
        }
    }
}
