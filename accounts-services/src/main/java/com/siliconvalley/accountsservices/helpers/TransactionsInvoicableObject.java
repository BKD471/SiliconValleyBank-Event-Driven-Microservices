package com.siliconvalley.accountsservices.helpers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class TransactionsInvoicableObject {
    private Timestamp transactionTimeStamp;
    private String transactionId;
    private BigDecimal transactionAmount;
    private String transactedAccountNumber;
    private String transactionType;
    private String description;
    private BigDecimal balance;
}
