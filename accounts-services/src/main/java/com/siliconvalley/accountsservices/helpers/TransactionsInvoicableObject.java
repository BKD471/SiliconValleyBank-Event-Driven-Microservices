package com.siliconvalley.accountsservices.helpers;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionsInvoicableObject {
    private Timestamp transactionTimeStamp;
    private String transactionId;
    private BigDecimal transactionAmount;
    private String transactedAccountNumber;
    private String transactionType;
    private String description;
    private BigDecimal balance;
}
